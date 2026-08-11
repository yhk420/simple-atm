package com.example.atm;

import java.util.List;

/**
 * Abstraction of the external bank system.
 */
public interface BankService {
    boolean validatePin(Card card, int pin);
    List<Account> getAccounts(Card card);
    int getBalance(String accountId);
    void deposit(String accountId, int amount);
    boolean withdraw(String accountId, int amount);
}