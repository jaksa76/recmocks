package com.zuhlke.testing.recmocks;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.zuhlke.testing.recmocks.RecMocks.recmock;

@RunWith(RecMocks.class)
public class AterClassInteractionTest {
    private static LegacyCrm legacyCrm = recmock(new LegacyCrm());
    private static Customer customer;

    @BeforeClass() public static void setUpClass() {
        customer = legacyCrm.getCustomer(1);
    }

    @Test public void testOne() throws Exception {
        legacyCrm.getCustomer(1);
    }

    @Test public void testTwo() throws Exception {
        legacyCrm.getCustomer(2);
    }

    @AfterClass public static void tearDownClass() {
        System.out.println(legacyCrm.toString());
    }
}
