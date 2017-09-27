package com.zuhlke.testing.recmocks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RecMocks.class)
public class TestRecording {

    @Before
    public void setUp() throws Exception {
        RecMocks.factory.setRecordMode(true);
    }

    @Test
    public void testRecording() throws Exception {
        File file = new File("recmocks/traces/com/zuhlke/testing/recmocks/TestRecording.testRecording.ArrayList.1.trace");
        file.delete();

        List<String> list = RecMocks.recmock(new ArrayList<String>());

        list.add("A");
        list.add("B");
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));

        assertTrue(file.exists());

        Trace trace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/TestRecording.testRecording.ArrayList.1.trace");
        Invocation invocation1 = trace.getNextInvocation();
        assertEquals("add", invocation1.getMethodName());
        assertEquals("A", invocation1.getArgs()[0]);
        Invocation invocation2 = trace.getNextInvocation();
        assertEquals("add", invocation2.getMethodName());
        assertEquals("B", invocation2.getArgs()[0]);
        Invocation invocation3 = trace.getNextInvocation();
        assertEquals("get", invocation3.getMethodName());
        assertEquals(0, invocation3.getArgs()[0]);
        assertEquals("A", invocation3.getReturnValue());
        Invocation invocation4 = trace.getNextInvocation();
        assertEquals("get", invocation4.getMethodName());
        assertEquals(1, invocation4.getArgs()[0]);
        assertEquals("B", invocation4.getReturnValue());
    }

    @Test
    public void testReplaying() throws Exception {
        Trace trace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/TestRecording.testReplaying.ArrayList.1.trace");
        trace.logInvocation(new Invocation("get", args(0), "A"));
        trace.logInvocation(new Invocation("get", args(1), "B"));

        RecMocks.factory.setRecordMode(false); // activate replay mode

        List<String> list = RecMocks.recmock(new ArrayList<String>());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    public void testCompositeObjects() {
        File crmFile = new File("recmocks/traces/com/zuhlke/testing/recmocks/TestRecording.testCompositeObjects.LegacyCrm.1.trace");
        crmFile.delete();
        File customerFile = new File("recmocks/traces/com/zuhlke/testing/recmocks/TestRecording.testCompositeObjects.LegacyCrm.1.trace");
        customerFile.delete();

        LegacyCrm crm = RecMocks.recmock(new LegacyCrm());
        Customer customer = crm.getCustomer(1);
        assertEquals("John", customer.getName());

        assertTrue(crmFile.exists());
        assertTrue(customerFile.exists());

        Trace crmTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/TestRecording.testCompositeObjects.LegacyCrm.1.trace");
        Invocation crmInvocation = crmTrace.getNextInvocation();
        assertEquals("getCustomer", crmInvocation.getMethodName());
        assertEquals(1, crmInvocation.getArgs()[0]);

        Trace customerTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/TestRecording.testCompositeObjects.Customer.2.trace");
        Invocation customerInvocation = customerTrace.getNextInvocation();
        assertEquals("getName", customerInvocation.getMethodName());
        assertEquals("John", customerInvocation.getReturnValue());
    }

    private Object[] args(Object... o) {
        return o;
    }
}
