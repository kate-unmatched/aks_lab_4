package com.coworking.booking.utils;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntitySnapshotUtil {

    public static Map<String, Object> extract(Object entity) {
        Map<String, Object> result = new LinkedHashMap<>();

        Class<?> clazz = entity.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(entity);

                if (isRelation(field)) {
                    continue;
                }

                result.put(field.getName(), value);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private static boolean isRelation(Field field) {
        return field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(ManyToMany.class);
    }
}
