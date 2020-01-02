package com.dtguai.cache.annotation;

import com.dtguai.cache.parser.KeyGenerator;
import com.dtguai.cache.parser.impl.DefaultKeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 清除缓存
 *
 * @author guo
 * @date 2019年12月16日16:26:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface CacheClear {

    /**
     * 缓存key的前缀
     * pre = "1*"
     * pre = {"1*","2","3*"}
     */
    String[] pre() default {};

    /**
     * 缓存key
     * key = "1"
     * key = {"1","2","3"}
     */
    String[] key() default {};

    /**
     * 键值解析类
     */
    Class<? extends KeyGenerator> generator() default DefaultKeyGenerator.class;
}
