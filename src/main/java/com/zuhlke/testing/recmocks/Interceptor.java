package com.zuhlke.testing.recmocks;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Interceptor of invocations that either works in record or replay mode.
 */
class Interceptor<T> implements MethodInterceptor {
    private final MockedObjectId id;
    private final T underlying;
    private final InterceptorFactory factory;
    private T proxy;

    Interceptor(MockedObjectId id, T underlying, InterceptorFactory factory) {
        this.id = id;
        this.underlying = underlying;
        this.factory = factory;
        this.proxy = newProxy();
    }

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = isRecordMode() ? record(o, method, args, methodProxy) : replay();
        if (result == null) return null;
        return isPrimitive(result) ? result : createInterceptor(result).getProxy();
    }

    private Interceptor<Object> createInterceptor(Object result) {
        if (result instanceof Invocation.ClassWrapper) {
            Class clazz = ((Invocation.ClassWrapper) result).c;
            return factory.createInterceptorForClass(clazz);
        }
        return factory.createInterceptor(result);
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
            Object returnValue = methodProxy.invoke(underlying, args);
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
        Class<?> clazz = underlying.getClass();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        Constructor constructor = getConstructorWithLeastParameters(clazz.getConstructors());
        int parameterCount = constructor.getParameterCount();
        if (parameterCount == 0) {
            return (T) enhancer.create();
        } else {
            return (T) enhancer.create(constructor.getParameterTypes(), new Object[parameterCount]);
        }
    }

    private Constructor getConstructorWithLeastParameters(Constructor[] constructors) {
        return Stream.of(constructors).min(Comparator.comparing(Constructor::getParameterCount)).get();
    }
}
