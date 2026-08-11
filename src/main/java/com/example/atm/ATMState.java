package com.example.atm;

/**
 * Represents the current stage of an ATM session.
 */
public enum ATMState {
    IDLE, CARD_INSERTED, AUTHENTICATED, ACCOUNT_SELECTED
}
