package com.dtguai.cache.service.impl;

import com.dtguai.cache.service.IRedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis实现类
 *
 * @author guo
 * @date 2019年12月4日15:02:50
 */
@Slf4j
@AllArgsConstructor
@Service
public class RedisServiceImpl implements IRedisService {

    private final RedisTemplate<String, String> redisTemplate;


    /**
     * 获取缓存信息
     *
     * @param key key
     * @return String
     */
    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 实现命令 : KEYS pattern
     * 查找所有符合 pattern 模式的 key
     * ? 匹配单个字符
     * * 匹配0到多个字符
     * [a-c] 匹配a和c
     * [ac] 匹配a到c
     * [^a] 匹配除了a以外的字符
     *
     * @param pattern redis pattern 表达式
     * @return Set<String>
     */
    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 插入缓存
     *
     * @param key    键
     * @param value  json内容
     * @param expire 超时时间 分钟
     */
    @Override
    public void set(String key, String value, int expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.MINUTES);
    }


    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(key);

    }

    @Override
    public Long del(Collection keys) {
        return redisTemplate.delete(keys);

    }


}
