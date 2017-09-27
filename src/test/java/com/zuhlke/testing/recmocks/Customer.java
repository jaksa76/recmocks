package com.zuhlke.testing.recmocks;

/**
 * A non-serializable class with no no-args constructor.
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
