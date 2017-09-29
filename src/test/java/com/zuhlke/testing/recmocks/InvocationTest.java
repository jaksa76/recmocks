package com.zuhlke.testing.recmocks;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.zuhlke.testing.recmocks.TestUtils.args;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class InvocationTest {
    @Test
    public void serializeUsingKryo() throws Exception {
        Kryo kryo = new Kryo();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, new Invocation("someMethod", args(1, 2, 3), "result"));
        output.flush();

        Input input = new Input(new ByteArrayInputStream(baos.toByteArray()));
        Invocation invocation = kryo.readObject(input, Invocation.class);
        assertEquals("someMethod", invocation.getMethodName());
        assertArrayEquals(args(1,2,3), invocation.getArgs());
        assertEquals("result", invocation.getReturnValue());
        assertEquals(String.class, invocation.getReturnClass());
    }
}