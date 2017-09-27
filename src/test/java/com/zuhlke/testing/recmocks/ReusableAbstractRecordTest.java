package com.zuhlke.testing.recmocks;

public class ReusableAbstractRecordTest {
    protected static LegacyCrm crm = RecMocks.recmock(new LegacyCrm());
    protected static Customer customer = crm.getCustomer(1);
    protected static String name = customer.getName();
}
