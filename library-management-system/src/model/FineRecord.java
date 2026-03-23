package model;

import java.time.LocalDate;

public class FineRecord {
    private int id;
    private int borrowerId;
    private double amount;
    private String reason;
    private LocalDate date;
    private PaymentMode mode;

    public FineRecord(int id, int borrowerId, double amount, String reason, LocalDate date, PaymentMode mode) {
        this.id = id;
        this.borrowerId = borrowerId;
        this.amount = amount;
        this.reason = reason;
        this.date = date;
        this.mode = mode;
    }

    public int getBorrowerId() { return borrowerId; }

    @Override
    public String toString() {
        return String.format("FineId:%d | Borrower:%d | Amount:%.2f | Reason:%s | Date:%s | PaidBy:%s",
                id, borrowerId, amount, reason, date, mode);
    }
}
