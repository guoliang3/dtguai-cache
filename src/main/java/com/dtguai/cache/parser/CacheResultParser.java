package com.dtguai.cache.parser;

import java.lang.reflect.Type;

/**
 * cache结果解析
 *
 * @param <T>
 * @author guo
 * @date 2019年12月10日19:06:52
 */
public interface CacheResultParser<T> {
    /**
     * 解析结果
     *
     * @param value      字符串内容
     * @param returnType 返回类型
     * @return T
     */
    T parse(String value, Type returnType);
}
