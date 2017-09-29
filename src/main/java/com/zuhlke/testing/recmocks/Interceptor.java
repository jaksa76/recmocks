package com.zuhlke.testing.recmocks;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.stream.Stream;

import static com.zuhlke.testing.recmocks.ObjectUtils.shouldSerialize;

/**
 * Interceptor of invocations that either works in record or replay mode.
 */
class Interceptor<T> implements MethodInterceptor {
    private final MockedObjectId id;
    private final T underlying;
    private final InterceptorFactory factory;
    private final Class<T> clazz;
    private T proxy;

    Interceptor(MockedObjectId id, T underlying, InterceptorFactory factory) {
        this.id = id;
        this.underlying = underlying;
        this.clazz = (Class<T>) underlying.getClass();
        this.factory = factory;
        this.proxy = newProxy();
    }

    Interceptor(MockedObjectId id, Class<T> c, InterceptorFactory factory) {
        this.id = id;
        this.clazz = c;
        this.underlying = null;
        this.factory = factory;
        this.proxy = newProxy();
    }

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = isRecordMode() ? record(o, method, args, methodProxy) : replay();
        if (result == null) return null;
        return shouldSerialize(result) ? result : createInterceptor(result).getProxy();
    }

    private Interceptor<Object> createInterceptor(Object result) {
        if (result instanceof Invocation.ClassWrapper) {
            Class clazz = ((Invocation.ClassWrapper) result).c;
            return factory.createInterceptorForClass(clazz);
        }
        return factory.createInterceptor(result);
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
        return createDynamicProxy(clazz, this);
    }

    private T createDynamicProxy(Class<?> clazz, Interceptor<T> interceptor) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            return (T) new Enhancer().create(findSuitableSuperclassOrInterface(clazz), this);
        }
        Constructor constructor = getConstructorWithLeastParameters(constructors);
        int parameterCount = constructor.getParameterCount();
        if (parameterCount == 0) {
            return (T) new Enhancer().create(clazz, interceptor);
        } else {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(this);
            return (T) enhancer.create(constructor.getParameterTypes(), new Object[parameterCount]);
        }
    }

    private Class<?> findSuitableSuperclassOrInterface(Class<?> clazz) {
        Class superclass = findSuitableSuperclass(clazz);
        if (superclass.equals(Object.class)) return findSuitableInterface(clazz);
        else return superclass;
    }

    private Class<?> findSuitableInterface(Class<?> clazz) {
        if (clazz.equals(Object.class)) return clazz;
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) return findSuitableInterface(clazz.getSuperclass());
        else return interfaces[0];
    }

    private Class findSuitableSuperclass(Class clazz) {
        if (hasNoArgsConstructor(clazz.getSuperclass())) return clazz.getSuperclass();
        else return findSuitableSuperclass(clazz.getSuperclass());
    }

    private boolean hasNoArgsConstructor(Class<?> superclass) {
        if (superclass == null) return false;
        if (superclass.isInterface()) return true;

        Constructor<?>[] constructors = superclass.getConstructors();
        if (constructors.length == 0) return false;

        return (getConstructorWithLeastParameters(constructors).getParameterCount() == 0);
    }

    private Constructor getConstructorWithLeastParameters(Constructor[] constructors) {
        return Stream.of(constructors).min(Comparator.comparing(Constructor::getParameterCount)).get();
    }
}
