package com.dtguai.cache.service;


import java.util.Collection;
import java.util.Set;

/**
 * redis 操作
 *
 * @author guo
 * @date 2019年12月10日13:28:09
 */
public interface IRedisService {

    /**
     * 通过key获取储存在redis中的value
     *
     * @param key key
     * @return 成功返回value 失败返回null
     */
    String get(String key);

    /**
     * <p>
     * 通过前缀获取储存在redis中的value
     * </p>
     * <p>
     * 并释放连接
     * </p>
     *
     * @param pattern key*,key?
     * @return 成功返回value 失败返回null
     */
    Set<String> keys(String pattern);


    /**
     * <p>
     * 向redis存入key和value,并释放连接资源
     * </p>
     * <p>
     * 如果key已经存在 则覆盖
     * </p>
     *
     * @param key    key
     * @param value  json
     * @param expire 超时时间
     */
    void set(String key, String value, int expire);


    /**
     * 删除 key
     *
     * @param key key
     * @return Boolean
     */
    Boolean del(String key);

    /**
     * <p>
     * 删除指定的key,也可以传入一个包含key的数组
     * </p>
     *
     * @param keys 一个key 也可以使 string 数组
     * @return 返回删除成功的个数
     */
    Long del(Collection<?> keys);


}