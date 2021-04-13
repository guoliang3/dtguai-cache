package com.dtguai.cache.test.unit;

import com.alibaba.fastjson.JSON;
import com.dtguai.cache.test.CacheTest;
import com.dtguai.cache.test.entity.User;
import com.dtguai.cache.test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guo
 * @date 2019年12月4日17:25:43
 */
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = CacheTest.class) // 指定我们SpringBoot工程的Application启动类
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetUser() {
        log.warn(JSON.toJSONString(userService.get("cx911")));
    }

    @Test
    public void testGetList() {
        log.warn(JSON.toJSONString(userService.getList()));
    }

    @Test
    public void testGetSet() {
        log.warn(JSON.toJSONString(userService.getSet()));
    }

    @Test
    public void testGetMap() {
        Map<String, Object> m = new HashMap<>(16);
        m.put("1", new User("1", 2, "3"));
        m.put("2", "22222");
        m.put("age", "3");
        m.put("name", "cx爱吃肉");

        List<Object> l = new ArrayList<>(20);
        l.add(new User("111", 222, "333"));
        l.add(1);
        l.add("cx");
        l.add(new User("11", 22, "33"));
        log.warn(JSON.toJSONString(userService.getMap(m, l, new User("fuck", 3, "cx-fuck"))));
    }

    @Test
    public void testSave() {
        userService.save(new User("fuck", 3, "cx-fuck"));
    }

    @Test
    public void testNullKey() {
        userService.nullKey(new User("fuck4", 4, "cx-fuck4"));
    }

    @Test
    public void testByKeyGenerator() {
        log.warn(JSON.toJSONString(userService.get(29)));
    }
}
