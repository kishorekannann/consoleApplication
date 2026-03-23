package model;

public class Book {
    private String isbn;
    private String name;
    private String author;
    private double cost;
    private int quantity;
    private int borrowCount;

    public Book(String isbn, String name, String author, double cost, int quantity) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.cost = cost;
        this.quantity = quantity;
    }

    public String getIsbn() { return isbn; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public double getCost() { return cost; }
    public int getQuantity() { return quantity; }
    public int getBorrowCount() { return borrowCount; }

    public void setName(String name) { this.name = name; }
    public void setAuthor(String author) { this.author = author; }
    public void setCost(double cost) { this.cost = cost; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void incrementBorrowCount() { this.borrowCount++; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | Cost: %.2f | Qty: %d | Borrowed: %d",
                isbn, name, author, cost, quantity, borrowCount);
    }
}
