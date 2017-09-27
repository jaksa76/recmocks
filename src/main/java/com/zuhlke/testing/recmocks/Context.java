package com.zuhlke.testing.recmocks;

import java.util.HashMap;
import java.util.Map;

class Context {
    private final Class testClass;
    private final String methodName;
    private int counter = 1;
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

    private String getInvoker() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // find where the Iterceptor is invoked
        int lastInterceptorInvocation = 0;
        for (int i = 0; i < stackTrace.length; i++) {
            if (stackTrace[i].getClassName().equals("com.zuhlke.testing.recmocks.Interceptor")) lastInterceptorInvocation = i;
        }

        // the invoker will invoke the dynamic proxy which will invoke the Interceptor
        String invoker = stackTrace[lastInterceptorInvocation + 2].getClassName();

        return invoker;
    }

    private String getPath(MockedObjectId id) {
        String path = "recmocks/traces/";

        if (testClass != null) {
            String packageName = testClass.getPackage().getName();
            String packagePath = packageName.replaceAll("\\.", "/");
            path += packagePath + "/" + testClass.getSimpleName() + ".";
            if (methodName != null) {
                path += methodName + ".";
            }
        } else {
            path += getInvoker().replaceAll("\\.", "/") + ".";
        }

        path += id;

        path += ".trace";

        return path;
    }
}
