package com.zuhlke.testing.recmocks;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.zuhlke.testing.recmocks.RecMocks.recmock;
import static com.zuhlke.testing.recmocks.TestUtils.args;
import static org.junit.Assert.assertEquals;

@RunWith(RecMocks.class)
public class FieldDeclarationInitializationReplayTest {
    private int dummy = generateTraces();

    private LegacyCrm crm = recmock(new LegacyCrm());
    private Customer customer = crm.getCustomer(1);
    private String customerName = customer.getName();

    private int generateTraces() {
        Trace crmTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/FieldDeclarationInitializationReplayTest.checkClassFields.LegacyCrm.1.trace");
        crmTrace.logInvocation(new Invocation("getCustomer", args(1), new Customer("Bob", "Doe")));
        crmTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/FieldDeclarationInitializationReplayTest.checkClassFieldsOnceMore.LegacyCrm.1.trace");
        crmTrace.logInvocation(new Invocation("getCustomer", args(1), new Customer("Bob", "Doe")));

        RecMocks.factory.setRecordMode(false);

        return 0;
    }

    @Test public void checkClassFields() throws Exception {
        assertEquals("Bob", customerName);
    }

    @Test public void checkClassFieldsOnceMore() throws Exception {
        assertEquals("Bob", customerName);
    }
}
