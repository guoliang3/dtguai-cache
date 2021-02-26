package com.dtguai.cache.aspect;

import com.dtguai.cache.annotation.Cache;
import com.dtguai.cache.api.CacheApi;
import com.dtguai.cache.config.DtRedisConfiguration;
import com.dtguai.cache.parser.CacheResultParser;
import com.dtguai.cache.parser.KeyGenerator;
import com.dtguai.cache.parser.impl.DefaultKeyGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存开启注解拦截
 *
 * @author guo
 * @date 2019年11月28日16:16:28
 */
@Aspect
@Service
@Slf4j
@AllArgsConstructor
public class CacheAspect {

    private final KeyGenerator keyParser;

    private final CacheApi cacheApi;

    private final DtRedisConfiguration dtRedisConfiguration;

    private final ConcurrentHashMap<String, CacheResultParser<?>> parserMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, KeyGenerator> generatorMap = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.dtguai.cache.annotation.Cache)")
    public void aspect() {

    }

    @Around("aspect()&&@annotation(cache)")
    public Object interceptor(ProceedingJoinPoint invocation, Cache cache) throws Throwable {
        MethodSignature signature = (MethodSignature) invocation.getSignature();
        Method method = signature.getMethod();
        Object result = null;
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = invocation.getArgs();
        String key = null;
        try {
            key = getKey(cache, parameterTypes, arguments);
            String value = cacheApi.get(key);
            //获取返回类型
            Type returnType = method.getGenericReturnType();
            result = getResult(cache, value, returnType);
        } catch (Exception e) {
            log.error("获取缓存失败：" + key, e);
        } finally {
            if (result == null) {
                result = invocation.proceed();
                if (StringUtils.isNotBlank(key)) {
                    cacheApi.set(key, result, cache.expire());
                }
            }
        }
        return result;
    }

    /**
     * 获取key生成实例
     *
     * @param cache          标签信息
     * @param parameterTypes 参数类型
     * @param arguments      传入参数
     * @return String
     */
    private String getKey(Cache cache, Class<?>[] parameterTypes, Object[] arguments) {

        String generatorClsName = cache.generator().getName();
        KeyGenerator keyGenerator = null;
        if (cache.generator().equals(DefaultKeyGenerator.class)) {
            keyGenerator = keyParser;
        } else {
            if (generatorMap.containsKey(generatorClsName)) {
                keyGenerator = generatorMap.get(generatorClsName);
            } else {
                try {
                    keyGenerator = cache.generator().newInstance();
                    generatorMap.put(generatorClsName, keyGenerator);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return Optional.ofNullable(keyGenerator)
                .map(x -> x.getKey(cache.key(), parameterTypes, arguments))
                .map(this::addSys)
                .orElse(null);
    }

    /**
     * 加入系统前缀
     */
    public String addSys(String key) {
        return dtRedisConfiguration.getApplicationName() + ":" + key;
    }

    /**
     * 解析结果
     *
     * @param cache      注解信息
     * @param value      缓存内容
     * @param returnType 返回类型
     * @return Object
     */
    private Object getResult(Cache cache, String value, Type returnType) {
        Object result = null;
        String parserClsName = cache.parser().getName();
        CacheResultParser<?> parser = null;
        if (parserMap.containsKey(parserClsName)) {
            parser = parserMap.get(parserClsName);
        } else {
            try {
                parser = cache.parser().newInstance();
                parserMap.put(parserClsName, parser);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        if (parser != null) {
            result = parser.parse(value, returnType);
        }
        return result;
    }
}
