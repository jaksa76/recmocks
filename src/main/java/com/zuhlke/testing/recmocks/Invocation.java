package com.zuhlke.testing.recmocks;

import java.io.*;
import java.util.Arrays;

/**
 * Represents an invocation in a {@link Trace}.
 */
public class Invocation implements Externalizable {
    private String methodName;
    private Object[] args;
    private Object returnValue;
    private Class returnClass;
    private Exception exception;

    public Invocation() {

    }

    Invocation(String methodName, Object[] args, Object returnValue) {
        this.methodName = methodName;
        this.args = args;
        this.returnClass = returnValue.getClass();
        this.returnValue = returnValue;
    }

    Invocation(String methodName, Object[] args, Exception exception) {
        this.methodName = methodName;
        this.args = args;
        this.exception = exception;
    }

    boolean doesThrowException() {
        return exception != null;
    }

    String getMethodName() {
        return methodName;
    }

    Object[] getArgs() {
        return args;
    }

    Object getReturnValue() {
        return returnValue;
    }

    public Class getReturnClass() {
        return returnClass;
    }

    Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "Invocation{" +
                "methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", returnValue=" + returnValue +
                ", exception=" + exception +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(methodName);
        Object[] argsForSerialization = new Object[args.length];
        for (int i = 0; i < args.length; i++) argsForSerialization[i] = marshal(args[i]);
        out.writeObject(argsForSerialization);
        out.writeObject(returnClass);
        out.writeObject(marshal(returnValue));
        out.writeObject(exception);
    }

    private Object marshal(Object object) {
        if (object == null) return null;
        return (object instanceof Serializable) ? object : new ClassWrapper(object.getClass());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        methodName = (String) in.readObject();
        args = (Object[]) in.readObject();
        returnClass = (Class) in.readObject();
        returnValue = in.readObject();
        exception = (Exception) in.readObject();
    }

    static class ClassWrapper implements Serializable {
        final Class c;
        ClassWrapper(Class c) { this.c = c; }
    }
}
