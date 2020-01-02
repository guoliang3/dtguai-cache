package com.dtguai.cache.api.impl;

import com.alibaba.fastjson.JSON;
import com.dtguai.cache.api.CacheApi;
import com.dtguai.cache.config.RedisConfiguration;
import com.dtguai.cache.service.IRedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 缓存API
 *
 * @author guo
 * @date 2019年12月16日17:34:06
 */
@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CacheRedis implements CacheApi {

    private RedisConfiguration redisConfiguration;

    private IRedisService redisCacheService;

    /**
     * 获取 缓存内容
     */
    @Override
    public String get(String key) {
        return Optional.ofNullable(key)
                .map(x -> redisCacheService.get(x))
                .orElse(null);
    }


    @Override
    public Boolean remove(String key) {
        return redisCacheService.del(key);
    }


    @Override
    public Long removeByPre(String pre) {
        return redisCacheService.del(redisCacheService.keys(pre));
    }


    @Override
    public void set(String key, Object value, int expireMin) {

        if (StringUtils.isBlank(key) || value == null) {
            log.error("key或value为空,key:{},value:{}", key, value);
            return;
        }

        String realValue;
        if (value instanceof String) {
            realValue = value.toString();
        } else {
            realValue = JSON.toJSONString(value);
        }

        redisCacheService.set(key, realValue, expireMin);
    }


}
