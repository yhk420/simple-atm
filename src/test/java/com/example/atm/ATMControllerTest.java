package com.example.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ATMControllerTest {

    private TestBankService bankService;
    private TestCashDispenser cashDispenser;
    private ATMController controller;

    @BeforeEach
    void setUp() {
        bankService = new TestBankService();
        cashDispenser = new TestCashDispenser(5000);
        controller = new ATMController(bankService, cashDispenser);
    }

    @Test
    void shouldCompleteNormalAtmFlow() {
        Card card = new Card("1111-2222-3333-4444");

        controller.insertCard(card);

        assertTrue(controller.enterPin(1234));

        List<Account> accounts = controller.getAccounts();
        assertFalse(accounts.isEmpty());

        controller.selectAccount("account-001");

        assertEquals(1000, controller.getBalance());

        controller.deposit(200);
        assertEquals(1200, controller.getBalance());

        controller.withdraw(300);
        assertEquals(900, controller.getBalance());
        assertEquals(4700, cashDispenser.getCash());
    }

    @Test
    void shouldRejectInvalidPin() {
        controller.insertCard(new Card("1111-2222-3333-4444"));

        assertFalse(controller.enterPin(9999));
    }

    @Test
    void shouldAllowRetryAfterIncorrectPin() {
        controller.insertCard(new Card("1111-2222-3333-4444"));

        assertFalse(controller.enterPin(1111));
        assertTrue(controller.enterPin(1234));

        assertFalse(controller.getAccounts().isEmpty());
    }

    @Test
    void shouldNotAllowAccountAccessBeforePinVerification() {
        controller.insertCard(new Card("1111-2222-3333-4444"));

        assertThrows(IllegalStateException.class, controller::getAccounts);
    }

    @Test
    void shouldRejectInvalidAccount() {
        controller.insertCard(new Card("1111-2222-3333-4444"));
        controller.enterPin(1234);

        assertThrows(IllegalArgumentException.class, () -> controller.selectAccount("account-999"));
    }

    @Test
    void shouldRejectWithdrawalWhenAccountBalanceIsInsufficient() {
        controller.insertCard(new Card("1111-2222-3333-4444"));
        controller.enterPin(1234);
        controller.selectAccount("account-001");

        assertThrows(IllegalStateException.class, () -> controller.withdraw(2000));

        assertEquals(1000, controller.getBalance());
        assertEquals(5000, cashDispenser.getCash());
    }

    @Test
    void shouldRejectWithdrawalWhenAtmCashIsInsufficient() {
        cashDispenser = new TestCashDispenser(100);
        controller = new ATMController(bankService, cashDispenser);

        controller.insertCard(new Card("1111-2222-3333-4444"));
        controller.enterPin(1234);
        controller.selectAccount("account-001");

        assertThrows(IllegalStateException.class, () -> controller.withdraw(500));

        assertEquals(1000, controller.getBalance());
        assertEquals(100, cashDispenser.getCash());
    }

    @Test
    void failedWithdrawalShouldNotChangeBalanceOrCash() {
        controller.insertCard(new Card("1111-2222-3333-4444"));
        controller.enterPin(1234);
        controller.selectAccount("account-001");

        int balanceBefore = controller.getBalance();
        int cashBefore = cashDispenser.getCash();

        assertThrows(IllegalStateException.class, () -> controller.withdraw(balanceBefore + 1));

        assertEquals(balanceBefore, controller.getBalance());
        assertEquals(cashBefore, cashDispenser.getCash());
    }

    @Test
    void shouldRejectNonPositiveDepositAmount() {
        controller.insertCard(new Card("1111-2222-3333-4444"));
        controller.enterPin(1234);
        controller.selectAccount("account-001");

        assertThrows(IllegalArgumentException.class, () -> controller.deposit(0));

        assertThrows(IllegalArgumentException.class, () -> controller.deposit(-100));
    }

    @Test
    void shouldRejectNonPositiveWithdrawalAmount() {
        controller.insertCard(new Card("1111-2222-3333-4444"));
        controller.enterPin(1234);
        controller.selectAccount("account-001");

        assertThrows(IllegalArgumentException.class, () -> controller.withdraw(0));

        assertThrows(IllegalArgumentException.class, () -> controller.withdraw(-100));
    }
}