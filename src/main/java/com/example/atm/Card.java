package com.example.atm;

/**
 * Represents a card inserted into the ATM.
 */
public class Card {

    private final String cardNumber;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}