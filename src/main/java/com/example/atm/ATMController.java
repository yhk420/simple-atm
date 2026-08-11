package com.example.atm;

import java.util.List;

/**
 * Controls the ATM workflow:
 * card insertion -> PIN verification -> account selection
 * -> balance inquiry, deposit, or withrawal.
 */
public class ATMController {
    private final BankService bankService;
    private final CashDispenser cashDispenser;

    private ATMState state;
    private Card currentCard;
    private Account selectedAccount;

    public ATMController(BankService bankService, CashDispenser cashDispenser) {
        this.bankService = bankService;
        this.cashDispenser = cashDispenser;
        this.state = ATMState.IDLE;
    }

    public void insertCard(Card card) {
        if (state != ATMState.IDLE) {
            throw new IllegalStateException("A card is already inserted.");
        }

        this.currentCard = card;
        this.state = ATMState.CARD_INSERTED;
    }

    public boolean enterPin(int pin) {
        if (state != ATMState.CARD_INSERTED) {
            throw new IllegalStateException("No card is ready for PIN verification.");
        }

        // PIN validation is delegated to the bank system
        boolean valid = bankService.validatePin(currentCard, pin);

        if (valid) {
            state = ATMState.AUTHENTICATED;
        }

        return valid;
    }


    public List<Account> getAccounts() {
        if (state != ATMState.AUTHENTICATED) {
            throw new IllegalStateException("PIN verification is required.");
        }
        return bankService.getAccounts(currentCard);
    }

    public void selectAccount(String accountId) {
        if (state != ATMState.AUTHENTICATED) {
            throw new IllegalStateException("PIN verification is required.");
        }

        // Only accounts associated with the inserted card can be selected.
        List<Account> accounts = bankService.getAccounts(currentCard);

        Account account = accounts.stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid account."));

        this.selectedAccount = account;
        this.state = ATMState.ACCOUNT_SELECTED;
    }

    private void ensureAccountSelected() {
        if (state != ATMState.ACCOUNT_SELECTED) {
            throw new IllegalStateException("An account must be selected.");
        }
    }

    public int getBalance() {
        ensureAccountSelected();

        return bankService.getBalance(selectedAccount.getAccountId());
    }

    // This ATM handles only one-dollar bills and dno cents.
    public void deposit(int amount) {
        ensureAccountSelected();

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        bankService.deposit(selectedAccount.getAccountId(), amount);
    }

    public void withdraw(int amount) {
        ensureAccountSelected();

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

        if (!cashDispenser.canDispense(amount)) {
            throw new IllegalStateException("ATM has insufficient cash.");
        }

        boolean success =
                bankService.withdraw(selectedAccount.getAccountId(), amount);

        if (!success) {
            throw new IllegalStateException("Insufficient account balance.");
        }

        cashDispenser.dispense(amount);
    }


}
