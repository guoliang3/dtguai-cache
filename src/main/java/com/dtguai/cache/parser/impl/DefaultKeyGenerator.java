package com.dtguai.cache.parser.impl;

import com.dtguai.cache.parser.KeyGenerator;
import com.dtguai.cache.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 默认键生成策略
 *
 * @author guo
 * @date 2019年12月10日13:23:32
 */
@Service
@Slf4j
public class DefaultKeyGenerator implements KeyGenerator {

    private static final String LEFT_BRACKET = "{";

    private static final String RIGHT_BRACKET = "}";

    /**
     * \d  匹配一个数字字符。等价于 [0-9]。
     * + 匹配前面的子表达式一次或多次。例如，'zo+' 能匹配 "zo" 以及 "zoo"，但不能匹配 "z"。+ 等价于 {1,}。
     * ? 匹配前面的子表达式零次或一次，或指明一个非贪婪限定符。要匹配 ? 字符，请使用 \?。
     * [ 标记一个中括号表达式的开始。要匹配 [，请使用 \[。
     * \w  匹配字母、数字、下划线。等价于'[A-Za-z0-9_]'。
     * * 匹配前面的子表达式零次或多次。要匹配 * 字符，请使用 \*。
     * 数字.[A-Za-z0-9_]
     */
    private static final Pattern PATTERN = Pattern.compile("\\d+\\.?[\\w]*");

    /**
     * 解析表达式
     *
     * @param key            key
     * @param parameterTypes 入参类型
     * @param arguments      入参
     * @return String key
     */
    @Override
    public String getKey(String key, Class<?>[] parameterTypes, Object[] arguments) {
        if (key.indexOf(LEFT_BRACKET) > 0) {
            Matcher matcher = PATTERN.matcher(key);
            while (matcher.find()) {
                String tmp = matcher.group();
                String[] express = matcher.group().split("\\.");
                String i = express[0];
                int index = Integer.parseInt(i) - 1;
                Object value = arguments[index];
                if (parameterTypes[index].isAssignableFrom(List.class)) {
                    value = ((List<?>) arguments[index]).get(0);
                } else if (parameterTypes[index].isAssignableFrom(Map.class)) {
                    value = ((Map<?, ?>) value).get(express[1]);
                }
                if (express.length > 1 && !parameterTypes[index].isAssignableFrom(Map.class) && value != null) {
                    String field = express[1];
                    value = ReflectionUtils.getFieldValue(value, field);
                }
                key = key.replace(LEFT_BRACKET + tmp + RIGHT_BRACKET, String.valueOf(value));

            }
        }
        return key;
    }


}
