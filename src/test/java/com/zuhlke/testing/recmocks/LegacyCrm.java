package com.zuhlke.testing.recmocks;

public class LegacyCrm {
    public Customer getCustomer(int id) {
        return new Customer("John", "Doe");
    }
}
