package com.zuhlke.testing.recmocks;

/**
 * Created by jvu on 9/27/2017.
 */
public class Customer {
    private final String name;
    private final String surname;

    public Customer(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
