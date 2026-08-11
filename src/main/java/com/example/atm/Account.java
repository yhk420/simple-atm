package com.example.atm;

/**
 * Represents a bank account available to the inserted card.
 */
public class Account {
    private final String accountId;
    private final String name;

    public Account(String accountId, String name) {
        this.accountId = accountId;
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }
}