package com.zuhlke.testing.recmocks;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import java.io.*;
import java.util.Arrays;

import static com.zuhlke.testing.recmocks.ObjectUtils.shouldSerialize;

/**
 * Represents an invocation in a {@link Trace}.
 */
public class Invocation implements KryoSerializable {
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
        this.returnClass = returnValue != null ? returnValue.getClass() : null;
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
    public void write(Kryo kryo, Output out) {
        kryo.writeObject(out, methodName);
        Object[] argsForSerialization = new Object[args.length];
        for (int i = 0; i < args.length; i++) argsForSerialization[i] = marshal(args[i]);
        kryo.writeClassAndObject(out, argsForSerialization);
        kryo.writeClassAndObject(out, returnClass);
        kryo.writeClassAndObject(out, marshal(returnValue));
        kryo.writeClassAndObject(out, exception);
    }

    private Object marshal(Object object) {
        if (object == null) return null;
        return shouldSerialize(object) ? object : new ClassWrapper(object.getClass());
    }

    @Override
    public void read(Kryo kryo, Input in) {
        methodName = kryo.readObject(in, String.class);
        args = (Object[]) kryo.readClassAndObject(in);
        returnClass = (Class) kryo.readClassAndObject(in);
        returnValue = kryo.readClassAndObject(in);
        exception = (Exception) kryo.readClassAndObject(in);
    }

    static class ClassWrapper implements Serializable {
        final Class c;
        ClassWrapper(Class c) { this.c = c; }
    }
}
