package com.xp.medshare.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtil {
    public static Map<String, Object> object2Map(Object o) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = o.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(o);
            map.put(fieldName, value);
        }
        return map;
    }
}
