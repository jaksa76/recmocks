package com.zuhlke.testing.recmocks;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(RecMocks.class)
public class DifferentContextTest {
    private LegacyCrm legacyCrm = RecMocks.recmock(new LegacyCrm());
    private Customer customer = legacyCrm.getCustomer(1);
    private String customerName = customer.getName();

    @Test
    public void testTraceFilesAreGenerated() throws Exception {
        Trace crmTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/DifferentContextTest.LegacyCrm.1.trace");
        Invocation crmInvocation = crmTrace.getNextInvocation();
        assertEquals("getCustomer", crmInvocation.getMethodName());
        assertEquals(1, crmInvocation.getArgs()[0]);

        Trace customerTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/DifferentContextTest.Customer.2.trace");
        Invocation customerInvocation = customerTrace.getNextInvocation();
        assertEquals("getName", customerInvocation.getMethodName());
        assertEquals("John", customerInvocation.getReturnValue());
    }
}
