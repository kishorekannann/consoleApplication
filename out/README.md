# ATM Console Application

This project is a console-based ATM simulator with a complete transaction flow instead of a minimal demo.

## Features

- Account login with PIN validation and account lock after 3 invalid attempts
- Balance enquiry with currency formatting
- Cash deposit
- Cash withdrawal with denomination validation in multiples of 100
- Daily withdrawal limit tracking
- Minimum balance enforcement
- Fund transfer between accounts
- PIN change with validation and confirmation
- Mini statement showing the latest transactions
- Persistent account state stored locally on disk for demo usage

## Sample Accounts

Use any of the following credentials to test the application:

| Account Number | PIN  | Name          |
| --- | --- | --- |
| 12345 | 1111 | Kishore Kumar |
| 67890 | 2222 | Priya Raman |
| 54321 | 3333 | Arun Das |

## Compile and Run

From the project root:

```bash
javac -d out $(find src -name "*.java")
java -cp out Main
```

## Notes

- Account data is persisted to data/accounts.ser after each update.
- Delete data/accounts.ser if you want to reset the app back to the seeded demo accounts.
- Transfers and withdrawals enforce a minimum remaining balance of 500.00.
- The daily withdrawal limit is 25000.00.
