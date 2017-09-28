package com.zuhlke.testing.recmocks;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class ObjectUtils {
    private static final Set<String> WRAPPER_TYPES = getWrapperTypes();

    static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz.getName());
    }

    public static boolean shouldSerialize(Object result) {
        return ObjectUtils.isWrapperType(result.getClass()) || result.getClass().equals(String.class);
    }

    private static Set<String> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret.stream().map(t -> t.getName()).collect(Collectors.toSet());
    }
}
