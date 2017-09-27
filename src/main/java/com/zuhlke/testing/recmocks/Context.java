package com.zuhlke.testing.recmocks;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines where the invocations are coming from.
 * It could be a field initialization of a test class, a @BeginClass method, a test method or
 * the lazy initialization in another class.
 */
class Context {
    private final Class testClass;
    private final String methodName;
    private int counter = 1; // used to create trace ids
    private Map<MockedObjectId, Trace> traces = new HashMap<MockedObjectId, Trace>();

    Context(Class testClass, String methodName) {
        this.testClass = testClass;
        this.methodName = methodName;
    }

    MockedObjectId generateId(Class objectClass) {
        return new MockedObjectId(objectClass.getSimpleName(), counter++);
    }

    Trace getTrace(MockedObjectId id) {
        return traces.computeIfAbsent(id, i -> new Trace(getPath(i)));
    }

    /**
     * @return the fully qualified class name of the object invoking the mock.
     */
    private String getInvoker() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // find where the Iterceptor is invoked
        int lastInterceptorInvocation = 0;
        for (int i = 0; i < stackTrace.length; i++) {
            if (stackTrace[i].getClassName().equals(Interceptor.class.getName())) lastInterceptorInvocation = i;
        }

        // the invoker will invoke the dynamic proxy which will invoke the Interceptor
        String invoker = stackTrace[lastInterceptorInvocation + 2].getClassName();

        return invoker;
    }

    private String getPath(MockedObjectId id) {
        String path = "recmocks/traces/";
        path += isInsideTestMethod() ? pathFromTestMethod() : pathFromInvoker();
        path += id;
        path += ".trace";
        return path;
    }

    private String pathFromInvoker() {
        return getInvoker().replaceAll("\\.", "/") + ".";
    }

    private String pathFromTestMethod() {
        return testClass.getName().replaceAll("\\.", "/") + "." + methodName + ".";
    }

    private boolean isInsideTestMethod() {
        return methodName != null;
    }
}
