package com.zuhlke.testing.recmocks;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Replayer<T> implements MethodInterceptor {
    private final Class<T> clazz;
    private final String id;

    public Replayer(Class<T> clazz, String id) {
        this.clazz = clazz;
        this.id = id;
        loadPreviousInteractions(id);
    }

    public T getInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    private void loadPreviousInteractions(String id) {
        // TODO
    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return null;
    }
}
