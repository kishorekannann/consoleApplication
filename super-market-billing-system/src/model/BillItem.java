package model;

public class BillItem {
    private int productId;
    private String productName;
    private double unitPrice;
    private int quantity;

    public BillItem(int productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }

    public double getLineTotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %.2f x %d = %.2f", productId, productName, unitPrice, quantity, getLineTotal());
    }
}
