package com.dtguai.cache.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dtguai.cache.annotation.Cache;
import com.dtguai.cache.annotation.CacheClear;
import com.dtguai.cache.parser.CacheResultParser;
import com.dtguai.cache.test.cache.MyKeyGenerator;
import com.dtguai.cache.test.entity.User;
import com.dtguai.cache.test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

/**
 * test
 *
 * @author guo
 * @date 2019年12月4日16:14:20
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Override
    @Cache(key = "user:{1}")
    public User get(String account) {
        log.warn("从get方法内读取....");
        return new User("太子", 42, account);
    }

    @Override
    @Cache(key = "user:list")
    public List<User> getList() {
        log.warn("从getLlist方法内读取....");
        List<User> users = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            User user = new User("陈雄", i, "cx911");
            users.add(user);
        }
        return users;
    }

    @Override
    @Cache(key = "user:set", parser = SetCacheResultParser.class)
    public Set<User> getSet() {
        log.warn("从getSet方法内读取....");
        Set<User> users = new HashSet<>(20);
        for (int i = 0; i < 20; i++) {
            User user = new User("陈雄", i, "cx911");
            users.add(user);
        }
        return users;
    }

    @Override
    @Cache(key = "user:map:{1.age}:{2.age}:{3.name}")
    public Map<String, User> getMap(Map<String, Object> m, List l, User u) {
        log.warn("从方法内读取....");
        Map<String, User> users = new HashMap<>(40);
        for (int i = 0; i < 20; i++) {
            User user = new User("陈雄", i, "cx911");
            users.put(user.getAccount() + i, user);
        }
        return users;
    }

    @Override
    //@CacheClear(key = "user:{1.age}:{1.name}:{1.account}", pre = "user", keys = "1,2,3,4")
    //@CacheClear( pre = "user:map:{1.age},2,3", keys = "1,2,3,4")
    @CacheClear(key = "1", pre = "2*")
    public void save(User user) {

    }

    @Override
    @Cache(generator = MyKeyGenerator.class)
    public User get(int age) {
        log.warn("从get方法内读取....");
        return new User("陈雄", age, "cx911");
    }

    /**
     * 对map返回结果做处理
     */
    public static class UserMapCacheResultParser implements CacheResultParser<HashMap<String, User>> {
        @Override
        public HashMap<String, User> parse(String value, Type returnType) {
            return JSON.parseObject(value, returnType);
        }
    }

    /**
     * 对set返回结果做处理
     */
    public static class SetCacheResultParser implements CacheResultParser<HashSet<User>> {
        @Override
        public HashSet<User> parse(String value, Type returnType) {
            return JSON.parseObject(value, new TypeReference<HashSet<User>>() {
            });
        }
    }
}