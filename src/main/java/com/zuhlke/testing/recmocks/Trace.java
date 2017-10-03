package com.zuhlke.testing.recmocks;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.SerializingInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;

class Trace {
    private Kryo kryo = new Kryo();
    private String path;
    private Output out;
    private Input in;

    Trace(String path) {
        this.path = path;
        kryo.setInstantiatorStrategy(new RecMocksInstantiationStrategy());
    }

    Invocation getNextInvocation() {
        try {
            if (this.in == null)
                this.in = new Input(new FileInputStream(path));
            return kryo.readObject(in, Invocation.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void logInvocation(Invocation invocation) {
        try {
            if (this.out == null) {
                createFile();
                this.out = new Output(new FileOutputStream(path));
            }
            kryo.writeObject(out, invocation);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File createFile() throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }
}
