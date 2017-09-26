package com.zuhlke.testing.recmocks;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

class Recorder<T> implements MethodInterceptor {
    private final T underlying;
    private final String id;
    private final File file;

    public Recorder(T underlying, String id) {
        this.underlying = underlying;
        this.id = id;
        this.file = new File("recmocks/traces/" + id);
        try {
            this.file.delete();
            file.getParentFile().mkdirs();
            this.file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Could not create trace file for " + id, e);
        }
    }

    public T getInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(underlying.getClass());
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // TODO record stuff
        return methodProxy.invoke(underlying, args);
    }
}
