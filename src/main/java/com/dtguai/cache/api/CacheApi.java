package com.dtguai.cache.api;

/**
 * 缓存API
 *
 * @author guo
 * @date 2019年12月16日16:40:40
 */
public interface CacheApi {

    /**
     * 获取 value
     *
     * @param key key
     * @return json或者String
     */
    String get(String key);

    /**
     * 保存缓存
     *
     * @param key       key
     * @param value     内容
     * @param expireMin 时间 分钟
     */
    void set(String key, Object value, int expireMin);


    /**
     * 移除单个缓存
     *
     * @param key key
     * @return Boolean
     */
    Boolean remove(String key);

    /**
     * 按前缀移除缓存
     *
     * @param pre key
     * @return Long
     */
    Long removeByPre(String pre);


}
