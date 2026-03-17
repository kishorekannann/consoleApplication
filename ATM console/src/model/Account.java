package model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String accountNumber;
    private final String holderName;
    private BigDecimal balance;
    private String pin;
    private boolean locked;
    private int failedLoginAttempts;
    private BigDecimal dailyWithdrawalTotal;
    private LocalDate withdrawalTrackingDate;
    private final List<Transaction> transactions;

    public Account(String accountNumber, String holderName, BigDecimal balance, String pin) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
        this.pin = pin;
        this.locked = false;
        this.failedLoginAttempts = 0;
        this.dailyWithdrawalTotal = BigDecimal.ZERO;
        this.withdrawalTrackingDate = LocalDate.now();
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getPin() {
        return pin;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public BigDecimal getDailyWithdrawalTotal() {
        refreshWithdrawalTrackingDate();
        return dailyWithdrawalTotal;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void changePin(String newPin) {
        pin = newPin;
    }

    public void registerFailedLoginAttempt(int maxLoginAttempts) {
        failedLoginAttempts++;
        if (failedLoginAttempts >= maxLoginAttempts) {
            locked = true;
        }
    }

    public void resetFailedLoginAttempts() {
        failedLoginAttempts = 0;
    }

    public void recordWithdrawal(BigDecimal amount) {
        refreshWithdrawalTrackingDate();
        dailyWithdrawalTotal = dailyWithdrawalTotal.add(amount);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    private void refreshWithdrawalTrackingDate() {
        if (!LocalDate.now().equals(withdrawalTrackingDate)) {
            withdrawalTrackingDate = LocalDate.now();
            dailyWithdrawalTotal = BigDecimal.ZERO;
        }
    }
}
