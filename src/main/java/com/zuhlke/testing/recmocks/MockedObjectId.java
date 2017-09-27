package com.zuhlke.testing.recmocks;

public class MockedObjectId {
    private final String objectClass;
    private final int id;

    public MockedObjectId(String objectClass, int id) {
        this.objectClass = objectClass;
        this.id = id;
    }

    @Override
    public String toString() {
        return objectClass + "." + id;
    }
}
