package com.zuhlke.testing.recmocks;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.zuhlke.testing.recmocks.TestUtils.args;

public class CollectionSerializationTest {
    @Test
    public void serializingHashset() throws Exception {
        Set<Long> set = new HashSet<Long>();

        Trace trace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/CollectionSerializationTest.trace");
        trace.logInvocation(new Invocation("get", args(), set));

        trace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/CollectionSerializationTest.trace");
        System.out.println(trace.getNextInvocation());
    }
}
