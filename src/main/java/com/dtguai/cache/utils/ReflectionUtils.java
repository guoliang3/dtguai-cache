package com.dtguai.cache.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 反射工具类.
 *
 * @author guo
 * @date 2019年12月17日16:27:29
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            return null;
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }


    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * <p/>
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
                log.error("Field不在当前类定义,继续向上转型 msg:{}",e.getMessage());
            }
        }
        return null;
    }


    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        boolean pub = Modifier.isPublic(field.getModifiers());
        boolean decPub = Modifier.isPublic(field.getDeclaringClass().getModifiers());
        boolean fin = Modifier.isFinal(field.getModifiers());
        boolean accessible = field.isAccessible();

        if ((!pub || !decPub || fin) && !accessible) {
            field.setAccessible(true);
        }
    }


}
