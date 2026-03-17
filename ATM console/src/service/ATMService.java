package service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import model.Transaction;
import model.TransactionType;
import model.Account;
import repository.AccountRepository;

public class ATMService {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final BigDecimal DAILY_WITHDRAWAL_LIMIT = new BigDecimal("25000.00");
    private static final BigDecimal MAX_SINGLE_TRANSACTION_LIMIT = new BigDecimal("50000.00");
    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("500.00");
    private final AccountRepository repo = new AccountRepository();

    public AuthenticationResult login(String accNo, String pin) {
        Account acc = repo.getAccount(accNo);
        if (acc == null) {
            return new AuthenticationResult(false, "Account not found.", null);
        }

        if (acc.isLocked()) {
            return new AuthenticationResult(false, "Account is locked after repeated invalid PIN attempts.", null);
        }

        if (!acc.getPin().equals(pin)) {
            acc.registerFailedLoginAttempt(MAX_LOGIN_ATTEMPTS);
            repo.update(acc);
            if (acc.isLocked()) {
                return new AuthenticationResult(false, "Account locked after 3 invalid PIN attempts.", null);
            }

            int remainingAttempts = MAX_LOGIN_ATTEMPTS - acc.getFailedLoginAttempts();
            return new AuthenticationResult(false, "Invalid PIN. Remaining attempts: " + remainingAttempts, null);
        }

        acc.resetFailedLoginAttempts();
        repo.update(acc);
        return new AuthenticationResult(true, "Login successful.", acc);
    }

    public OperationResult deposit(Account acc, BigDecimal amount) {
        BigDecimal validAmount = validateAmount(amount, false);
        acc.credit(validAmount);
        acc.addTransaction(new Transaction(
                TransactionType.DEPOSIT,
                validAmount,
                acc.getBalance(),
                "Cash deposit",
                LocalDateTime.now()));
        repo.update(acc);
        return new OperationResult(true, "Deposit completed successfully.", acc.getBalance());
    }

    public OperationResult withdraw(Account acc, BigDecimal amount) {
        BigDecimal validAmount = validateAmount(amount, true);
        validateWithdrawalRules(acc, validAmount);
        acc.debit(validAmount);
        acc.recordWithdrawal(validAmount);
        acc.addTransaction(new Transaction(
                TransactionType.WITHDRAWAL,
                validAmount,
                acc.getBalance(),
                "Cash withdrawal",
                LocalDateTime.now()));
        repo.update(acc);
        return new OperationResult(true, "Withdrawal completed successfully.", acc.getBalance());
    }

    public OperationResult transfer(Account source, String targetAccountNumber, BigDecimal amount) {
        BigDecimal validAmount = validateAmount(amount, false);
        Account target = repo.getAccount(targetAccountNumber);

        if (target == null) {
            throw new IllegalArgumentException("Destination account does not exist.");
        }

        if (source.getAccountNumber().equals(targetAccountNumber)) {
            throw new IllegalArgumentException("Source and destination accounts must be different.");
        }

        if (source.getBalance().subtract(validAmount).compareTo(MINIMUM_BALANCE) < 0) {
            throw new IllegalArgumentException("Transfer would violate the minimum balance requirement of 500.00.");
        }

        source.debit(validAmount);
        target.credit(validAmount);

        source.addTransaction(new Transaction(
                TransactionType.TRANSFER_OUT,
                validAmount,
                source.getBalance(),
                "Transfer to account " + target.getAccountNumber(),
                LocalDateTime.now()));
        target.addTransaction(new Transaction(
                TransactionType.TRANSFER_IN,
                validAmount,
                target.getBalance(),
                "Transfer from account " + source.getAccountNumber(),
                LocalDateTime.now()));

        repo.update(source);
        repo.update(target);
        return new OperationResult(true, "Transfer completed successfully.", source.getBalance());
    }

    public OperationResult changePin(Account acc, String currentPin, String newPin) {
        if (!acc.getPin().equals(currentPin)) {
            throw new IllegalArgumentException("Current PIN is incorrect.");
        }

        if (!newPin.matches("\\d{4}")) {
            throw new IllegalArgumentException("New PIN must be exactly 4 digits.");
        }

        if (newPin.equals(currentPin)) {
            throw new IllegalArgumentException("New PIN must be different from the current PIN.");
        }

        acc.changePin(newPin);
        acc.addTransaction(new Transaction(
                TransactionType.PIN_CHANGE,
                BigDecimal.ZERO,
                acc.getBalance(),
                "PIN changed",
                LocalDateTime.now()));
        repo.update(acc);
        return new OperationResult(true, "PIN changed successfully.", acc.getBalance());
    }

    public BigDecimal checkBalance(Account acc) {
        return acc.getBalance();
    }

    public List<Transaction> getMiniStatement(Account acc) {
        return acc.getTransactions();
    }

    public boolean accountExists(String accountNumber) {
        return repo.accountExists(accountNumber);
    }

    private BigDecimal validateAmount(BigDecimal amount, boolean cashOperation) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required.");
        }

        BigDecimal normalizedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        if (normalizedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (normalizedAmount.compareTo(MAX_SINGLE_TRANSACTION_LIMIT) > 0) {
            throw new IllegalArgumentException("Amount exceeds the single transaction limit of 50000.00.");
        }

        if (cashOperation && normalizedAmount.remainder(new BigDecimal("100")).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Cash withdrawals must be in multiples of 100.");
        }

        return normalizedAmount;
    }

    private void validateWithdrawalRules(Account acc, BigDecimal amount) {
        if (acc.getBalance().subtract(amount).compareTo(MINIMUM_BALANCE) < 0) {
            throw new IllegalArgumentException("Withdrawal would violate the minimum balance requirement of 500.00.");
        }

        BigDecimal projectedDailyTotal = acc.getDailyWithdrawalTotal().add(amount);
        if (projectedDailyTotal.compareTo(DAILY_WITHDRAWAL_LIMIT) > 0) {
            throw new IllegalArgumentException("Daily withdrawal limit of 25000.00 exceeded.");
        }
    }
}
