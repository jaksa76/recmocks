package com.zuhlke.testing.recmocks;

import static com.zuhlke.testing.recmocks.TestUtils.args;

public class ReusableAbstractReplayTest {
    protected static int dummy = initializeFields();
    protected static LegacyCrm crm;
    protected static Customer customer;
    protected static String name;

    private static int initializeFields() {
        Trace crmTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/ReusableAbstractReplayTest.LegacyCrm.1.trace");
        crmTrace.logInvocation(new Invocation("getCustomer", args(1), new Customer("Bob", "Doe")));

//        Trace customerTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/ReusableAbstractReplayTest.Customer.2.trace");
//        customerTrace.logInvocation(new Invocation("gatName", args(), "Bob"));

        RecMocks.factory.setRecordMode(false);

        crm = RecMocks.recmock(new LegacyCrm());
        customer = crm.getCustomer(1);
        name = customer.getName();

        return 0;
    }
}
