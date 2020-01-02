package com.dtguai.cache.annotation;

import com.dtguai.cache.parser.CacheResultParser;
import com.dtguai.cache.parser.KeyGenerator;
import com.dtguai.cache.parser.impl.DefaultKeyGenerator;
import com.dtguai.cache.parser.impl.DefaultResultParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启缓存
 *
 * @author guo
 * @date 2019年11月28日16:08:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface Cache {

    /**
     * 缓存key menu_{0.id}{1}_type
     */
    String key() default "";

    /**
     * 过期时间
     */
    int expire() default 10;

    /**
     * 返回结果解析类
     */
    Class<? extends CacheResultParser> parser() default DefaultResultParser.class;

    /**
     * 键值解析类
     */
    Class<? extends KeyGenerator> generator() default DefaultKeyGenerator.class;
}
