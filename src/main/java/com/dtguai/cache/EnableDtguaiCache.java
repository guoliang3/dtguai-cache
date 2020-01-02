package com.dtguai.cache;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author guo
 * @date 2019年11月28日16:00:59
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfiguration.class)
@Documented
@Inherited
public @interface EnableDtguaiCache {
}
