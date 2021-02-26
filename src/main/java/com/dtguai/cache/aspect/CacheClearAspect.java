package com.dtguai.cache.aspect;

import com.dtguai.cache.annotation.CacheClear;
import com.dtguai.cache.api.CacheApi;
import com.dtguai.cache.config.DtRedisConfiguration;
import com.dtguai.cache.parser.KeyGenerator;
import com.dtguai.cache.parser.impl.DefaultKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 清除缓存注解拦截
 *
 * @author guo
 * @date 2019年12月4日17:13:29
 */
@Aspect
@Service
@Slf4j
public class CacheClearAspect {

    @Autowired
    private KeyGenerator keyParser;

    @Autowired
    private CacheApi cacheApi;

    @Autowired
    private DtRedisConfiguration dtRedisConfiguration;

    private ConcurrentHashMap<String, KeyGenerator> generatorMap = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.dtguai.cache.annotation.CacheClear)")
    public void aspect() {
    }

    @Around("aspect()&&@annotation(cacheClear)")
    public Object interceptor(ProceedingJoinPoint invocation, CacheClear cacheClear) throws Throwable {
        MethodSignature signature = (MethodSignature) invocation.getSignature();
        Method method = signature.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = invocation.getArgs();
        String key;
        if (cacheClear.key().length > 0) {
            for (String s : cacheClear.key()) {
                key = getKey(cacheClear, s, parameterTypes, arguments);
                cacheApi.remove(key);
            }
        }
        if (cacheClear.pre().length > 0) {
            for (String s : cacheClear.pre()) {
                key = getKey(cacheClear, s, parameterTypes, arguments);
                cacheApi.removeByPre(key);
            }
        }
        return invocation.proceed();
    }

    /**
     * 解析表达式
     *
     * @param anno           标签信息
     * @param key            缓存key
     * @param parameterTypes 参数类型
     * @param arguments      参数
     * @return String
     */
    private String getKey(CacheClear anno, String key, Class<?>[] parameterTypes, Object[] arguments) {
        String generatorClsName = anno.generator().getName();
        KeyGenerator keyGenerator = null;
        if (anno.generator().equals(DefaultKeyGenerator.class)) {
            keyGenerator = keyParser;
        } else {
            if (generatorMap.containsKey(generatorClsName)) {
                keyGenerator = generatorMap.get(generatorClsName);
            } else {
                try {
                    keyGenerator = anno.generator().newInstance();
                    generatorMap.put(generatorClsName, keyGenerator);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return Optional.ofNullable(keyGenerator)
                .map(x -> x.getKey(key, parameterTypes, arguments))
                .map(this::addSys)
                .orElse(null);
    }

    /**
     * 加入系统前缀
     */
    public String addSys(String key) {
        return dtRedisConfiguration.getApplicationName() + ":" + key;
    }

}
