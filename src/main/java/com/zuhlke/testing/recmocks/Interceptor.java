package com.zuhlke.testing.recmocks;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Interceptor of invocations that either works in record or replay mode.
 */
class Interceptor<T> implements MethodInterceptor {
    private final MockedObjectId id;
    private final T underlying;
    private final Class<T> clazz;
    private final InterceptorFactory factory;
    private T proxy;

    Interceptor(MockedObjectId id, T underlying, InterceptorFactory factory) {
        this.id = id;
        this.underlying = underlying;
        this.clazz = (Class<T>) underlying.getClass();
        this.factory = factory;
        this.proxy = newProxy();
    }

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = isRecordMode() ? record(o, method, args, methodProxy) : replay();
        return isPrimitive(result) ? result : factory.createInterceptor(result);
    }

    private boolean isPrimitive(Object result) {
        return ObjectUtils.isWrapperType(result.getClass()) || result.getClass().equals(String.class);
    }

    private Object replay() throws Exception {
        Invocation invocation = getTrace().getNextInvocation();
        if (invocation == null) throw new RuntimeException("Unexpected invocation");
        if (invocation.doesThrowException()) throw invocation.getException();
        else return invocation.getReturnValue();
    }

    private Object record(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Invocation invocation = null;
        try {
            Object returnValue = methodProxy.invokeSuper(o, args);
            invocation = new Invocation(method.getName(), args, returnValue);
            return returnValue;
        } catch (Exception e) {
            invocation = new Invocation(method.getName(), args, e);
            throw e;
        } finally {
            getTrace().logInvocation(invocation);
        }
    }

    private Trace getTrace() {
        return factory.getCurrentContext().getTrace(id);
    }

    private boolean isRecordMode() {
        return factory.isRecordMode();
    }

    T getProxy() {
        return proxy;
    }

    private T newProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(underlying.getClass());
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }
}
