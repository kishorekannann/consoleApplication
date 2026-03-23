package model;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int borrowerId;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private int extensionCount;
    private boolean returned;
    private boolean lost;

    public Loan(int id, int borrowerId, String isbn, LocalDate borrowDate) {
        this.id = id;
        this.borrowerId = borrowerId;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(15);
    }

    public int getId() { return id; }
    public int getBorrowerId() { return borrowerId; }
    public String getIsbn() { return isbn; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public int getExtensionCount() { return extensionCount; }
    public boolean isReturned() { return returned; }
    public boolean isLost() { return lost; }

    public boolean canExtend() { return extensionCount < 2; }

    public void extendBy15Days() {
        if (canExtend()) {
            this.dueDate = this.dueDate.plusDays(15);
            this.extensionCount++;
        }
    }

    public void markReturned(LocalDate date) {
        this.returned = true;
        this.returnDate = date;
    }

    public void markLost(LocalDate date) {
        this.lost = true;
        this.returned = true;
        this.returnDate = date;
    }

    @Override
    public String toString() {
        return String.format("LoanId:%d | Borrower:%d | ISBN:%s | Borrow:%s | Due:%s | Returned:%s | Lost:%s | Ext:%d",
                id, borrowerId, isbn, borrowDate, dueDate, returned, lost, extensionCount);
    }
}
