package com.dtguai.cache.test;

import com.dtguai.cache.EnableDtguaiCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * test
 *
 * @author guo
 * @date 2019年12月16日16:43:51
 */
@SpringBootApplication
@EnableDtguaiCache
public class CacheTest {
    public static void main(String args[]) {
        SpringApplication app = new SpringApplication(CacheTest.class);
        app.run(args);
    }

}
