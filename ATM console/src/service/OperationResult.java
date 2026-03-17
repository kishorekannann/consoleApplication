package service;

import java.math.BigDecimal;

public class OperationResult {
    private final boolean success;
    private final String message;
    private final BigDecimal balance;

    public OperationResult(boolean success, String message, BigDecimal balance) {
        this.success = success;
        this.message = message;
        this.balance = balance;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}