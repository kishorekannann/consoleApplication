package model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final TransactionType type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfterTransaction;
    private final String description;
    private final LocalDateTime timestamp;

    public Transaction(
            TransactionType type,
            BigDecimal amount,
            BigDecimal balanceAfterTransaction,
            String description,
            LocalDateTime timestamp) {
        this.type = type;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.description = description;
        this.timestamp = timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}