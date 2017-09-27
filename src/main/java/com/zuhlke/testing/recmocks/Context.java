package com.zuhlke.testing.recmocks;

import java.io.File;

class Context {
    private final Class testClass;
    private final String methodName;
    private int counter = 1;

    Context(Class testClass, String methodName) {
        this.testClass = testClass;
        this.methodName = methodName;
    }

    MockedObjectId generateId(Class objectClass) {
        return new MockedObjectId(objectClass.getSimpleName(), counter++);
    }

    Trace getTrace(Class objectClass, MockedObjectId id) {
        return new Trace(getPath(objectClass, id));
    }

    private String getPath(Class objectClass, MockedObjectId id) {
        String path = "recmocks/traces/";

        if (testClass != null) {
            String packageName = testClass.getPackage().getName();
            String packagePath = packageName.replaceAll("\\.", "/");
            path += packagePath + "/" + testClass.getSimpleName() + ".";
            if (methodName != null) {
                path += methodName + ".";
            }
        }

        path += id;

        path += ".trace";

        return path;
    }
}
