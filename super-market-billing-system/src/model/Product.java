package model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int soldQuantity;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getSoldQuantity() { return soldQuantity; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void increaseQuantity(int qty) { this.quantity += qty; }

    public boolean reduceQuantity(int qty) {
        if (qty > this.quantity) {
            return false;
        }
        this.quantity -= qty;
        this.soldQuantity += qty;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | Price: %.2f | Qty: %d | Sold: %d", id, name, price, quantity, soldQuantity);
    }
}
