package com.example.atm;

/**
 * Abstraction of the ATM cash dispensing hardware.
 */
public interface CashDispenser {
    boolean canDispense(int amount);

    void dispense(int amount);
}