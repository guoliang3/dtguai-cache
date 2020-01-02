
package com.dtguai.cache.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author guo
 * @date 2019年12月10日11:42:25
 */
@Configuration
@Data
public class RedisConfiguration {

    /**
     * 服务名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    private LettuceConnectionFactory factory;

    @Autowired
    public RedisConfiguration(LettuceConnectionFactory factory) {
        this.factory = factory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);

        return redisTemplate;
    }

}
