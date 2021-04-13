package com.dtguai.cache.test.service;

import com.dtguai.cache.test.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author guo
 * @date 2019年12月11日14:55:42
 */
public interface UserService {
    User get(String account);

    List<User> getList();

    Set<User> getSet();

    Map<String, User> getMap(Map<String, Object> m, List l, User user);

    void save(User user);

    /**
     * 空key
     *
     * @param user user
     * @return User
     */
    User nullKey(User user);

    User get(int age);
}
