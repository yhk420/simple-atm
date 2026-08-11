package com.example.atm;

/**
 * Test implementation of CashDispenser
 */
public class TestCashDispenser implements CashDispenser {

    private int cash;

    public TestCashDispenser(int cash) {
        this.cash = cash;
    }

    @Override
    public boolean canDispense(int amount) {
        return cash >= amount;
    }

    @Override
    public void dispense(int amount) {
        cash -= amount;
    }

    public int getCash() {
        return cash;
    }
}