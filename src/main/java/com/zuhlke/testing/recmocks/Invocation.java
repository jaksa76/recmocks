package com.zuhlke.testing.recmocks;

import java.io.Serializable;

public class Invocation implements Serializable {
    private String methodName;
    private Object[] args;
    private Object returnValue;
    private Exception exception;

    public Invocation(String methodName, Object[] args, Object returnValue) {
        this.methodName = methodName;
        this.args = args;
        this.returnValue = returnValue;
    }

    public Invocation(String methodName, Object[] args, Exception exception) {
        this.methodName = methodName;
        this.args = args;
        this.exception = exception;
    }

    public boolean doesThrowException() {
        return exception != null;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public Exception getException() {
        return exception;
    }
}
