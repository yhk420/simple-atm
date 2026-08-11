# Simple ATM

A simple ATM controller implemented in Java 21.

The project focuses on controller logic without UI, real bank integration, or ATM hardware integration.

## Features

* Insert card
* Verify PIN
* Select account
* Check balance
* Deposit
* Withdraw

All monetary values are integers. Cents are not supported.

## Design

`ATMController` manages the ATM flow and session state.

External systems are separated by interfaces:

* `BankService` - bank operations and PIN verification
* `CashDispenser` - ATM cash dispensing

This allows the controller to be tested without real bank or ATM systems and makes future integration easier.

The ATM does not retrieve or store the actual PIN. PIN verification is handled by `BankService`.

## Requirements

* Java 21
* Maven

## Clone

```bash
git clone https://github.com/yhk420/simple-atm.git
cd simple-atm
```

## Build

```bash
mvn clean compile
```

## Run Tests

```bash
mvn test
```

Or clean and test together:

```bash
mvn clean test
```

## Project Structure

```text
src
├── main
│   └── java/com/example/atm
│       ├── ATMController.java
│       ├── ATMState.java
│       ├── Account.java
│       ├── BankService.java
│       ├── Card.java
│       └── CashDispenser.java
│
└── test
    └── java/com/example/atm
        ├── ATMControllerTest.java
        ├── TestBankService.java
        └── TestCashDispenser.java
```

## Tests

Tests cover the main ATM flow and error cases:

* Complete ATM flow
* Invalid PIN
* Retry after an incorrect PIN
* Access before PIN verification
* Invalid account selection
* Deposit and withdrawal
* Insufficient account balance
* Insufficient ATM cash
* Invalid transaction amounts
* No balance or ATM cash change after a failed withdrawal

