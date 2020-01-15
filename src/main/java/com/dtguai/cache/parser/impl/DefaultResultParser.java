package com.dtguai.cache.parser.impl;

import com.alibaba.fastjson.JSON;
import com.dtguai.cache.parser.CacheResultParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 默认缓存结果解析类
 *
 * @author guo
 * @date 2019年12月24日15:37:19
 */
public class DefaultResultParser implements CacheResultParser<Object> {

    /**
     * 默认缓存结果解析
     *
     * @param value json内容
     * @param type  返回类型
     * @return Object
     */
    @Override
    public Object parse(String value, Type type) {
        Object result = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (((Class<?>) rawType).isAssignableFrom(List.class)) {
                //result = JSON.parseArray(value, (Class<?>) parameterizedType.getActualTypeArguments()[0]);
                result = JSON.parseArray(value, type.getClass());
            }
        }
        return result == null ? JSON.parseObject(value, type) : result;
    }
}
