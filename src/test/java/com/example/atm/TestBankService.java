package com.example.atm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test implementation of BankService
 */
public class TestBankService implements BankService {

    private final Card card = new Card("1111-2222-3333-4444");
    private final int pin = 1234;

    private final List<Account> accounts = new ArrayList<>();
    private final Map<String, Integer> balances = new HashMap<>();

    public TestBankService() {
        accounts.add(new Account("account-001", "Primary Account"));
        accounts.add(new Account("account-002", "Secondary Account"));

        balances.put("account-001", 1000);
        balances.put("account-002", 2000);
    }

    @Override
    public boolean validatePin(Card card, int pin) {
        return this.card.getCardNumber().equals(card.getCardNumber()) && this.pin == pin;
    }

    @Override
    public List<Account> getAccounts(Card card) {
        if (!this.card.getCardNumber().equals(card.getCardNumber())) {
            return List.of();
        }

        return List.copyOf(accounts);
    }

    @Override
    public int getBalance(String accountId) {
        return balances.get(accountId);
    }

    @Override
    public void deposit(String accountId, int amount) {
        balances.put(accountId, balances.get(accountId) + amount);
    }

    @Override
    public boolean withdraw(String accountId, int amount) {
        int balance = balances.get(accountId);

        if (balance < amount) {
            return false;
        }

        balances.put(accountId, balance - amount);
        return true;
    }
}