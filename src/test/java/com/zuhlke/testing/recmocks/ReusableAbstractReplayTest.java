package com.zuhlke.testing.recmocks;

import static com.zuhlke.testing.recmocks.TestUtils.args;

public class ReusableAbstractReplayTest {
    protected static int dummy = initializeFields();
    protected static LegacyCrm crm;
    protected static Customer customer;
    protected static String name;

    private static int initializeFields() {
        Trace crmTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/ReplayTestSubclass1.checkTraces.LegacyCrm.1.trace");
        crmTrace.logInvocation(new Invocation("getCustomer", args(1), new Customer("Bob", "Doe")));
        crmTrace = new Trace("recmocks/traces/com/zuhlke/testing/recmocks/ReplayTestSubclass2.checkTraces.LegacyCrm.1.trace");
        crmTrace.logInvocation(new Invocation("getCustomer", args(1), new Customer("Bob", "Doe")));

        RecMocks.factory.setRecordMode(false);

        crm = RecMocks.recmock(new LegacyCrm());
        customer = crm.getCustomer(1);
        name = customer.getName();

        return 0;
    }
}
