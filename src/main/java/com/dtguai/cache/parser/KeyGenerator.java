package com.dtguai.cache.parser;

/**
 * 缓存键值表达式
 *
 * @author guo
 * @date 2019年12月10日13:24:12
 */
public interface KeyGenerator {


    /**
     * 获取动态key
     *
     * @param key            缓存名
     * @param parameterTypes 入参类型
     * @param arguments      入参
     * @return String
     */
    String getKey(String key, Class<?>[] parameterTypes, Object[] arguments);

}
