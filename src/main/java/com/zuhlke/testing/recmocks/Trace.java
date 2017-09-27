package com.zuhlke.testing.recmocks;

import java.io.*;
import java.nio.file.Files;

class Trace {
    private String path;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    Trace(String path) {
        this.path = path;
    }

    Invocation getNextInvocation() {
        try {
            if (this.in == null)
                this.in = new ObjectInputStream(new FileInputStream(path));
            return (Invocation) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void logInvocation(Invocation invocation) {
        try {
            if (this.out == null) {
                createFile();
                this.out = new ObjectOutputStream(new FileOutputStream(path));
            }
            out.writeObject(invocation);
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
