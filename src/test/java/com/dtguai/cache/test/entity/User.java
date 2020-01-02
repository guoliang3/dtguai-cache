package com.dtguai.cache.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author guo
 * @date 2019年12月4日16:37:19
 */
@Data
@AllArgsConstructor
public class User {
    private String name;
    private int age;
    private String account;
}
