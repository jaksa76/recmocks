package com.zuhlke.testing.recmocks;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(RecMocks.class)
public class RecordTestSubclass1 extends ReusableAbstractRecordTest {
    @Test
    public void checkTraces() throws Exception {
        Trace crmTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/ReusableAbstractRecordTest.LegacyCrm.1.trace");
        Invocation crmInvocation = crmTrace.getNextInvocation();
        assertEquals("getCustomer", crmInvocation.getMethodName());
        assertEquals(1, crmInvocation.getArgs()[0]);

//        Trace customerTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/ReusableAbstractRecordTest.Customer.2.trace");
//        Invocation customerInvocation = customerTrace.getNextInvocation();
//        assertEquals("getName", customerInvocation.getMethodName());
//        assertEquals("John", customerInvocation.getReturnValue());
    }
}
