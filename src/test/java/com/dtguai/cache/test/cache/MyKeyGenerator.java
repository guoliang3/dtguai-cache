package com.dtguai.cache.test.cache;

import com.dtguai.cache.parser.KeyGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author guo
 * @date 2019年12月4日16:55:30
 */
@Slf4j
public class MyKeyGenerator implements KeyGenerator {

    @Override
    public String getKey(String key, Class<?>[] parameterTypes, Object[] arguments) {
        return "myKey_"; //+ arguments[0];
    }
}
