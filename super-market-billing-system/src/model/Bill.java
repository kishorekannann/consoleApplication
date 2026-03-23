package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int billNumber;
    private int customerId;
    private int billedByAdminId;
    private LocalDateTime billDate;
    private List<BillItem> items = new ArrayList<>();

    private double grossAmount;
    private double discountApplied;
    private double payableAmount;
    private int loyaltyPointsEarned;
    private String rewardNote;

    public Bill(int billNumber, int customerId, int billedByAdminId, LocalDateTime billDate) {
        this.billNumber = billNumber;
        this.customerId = customerId;
        this.billedByAdminId = billedByAdminId;
        this.billDate = billDate;
    }

    public int getBillNumber() { return billNumber; }
    public int getCustomerId() { return customerId; }
    public int getBilledByAdminId() { return billedByAdminId; }
    public LocalDateTime getBillDate() { return billDate; }
    public List<BillItem> getItems() { return items; }
    public double getGrossAmount() { return grossAmount; }
    public double getDiscountApplied() { return discountApplied; }
    public double getPayableAmount() { return payableAmount; }
    public int getLoyaltyPointsEarned() { return loyaltyPointsEarned; }
    public String getRewardNote() { return rewardNote; }

    public void setGrossAmount(double grossAmount) { this.grossAmount = grossAmount; }
    public void setDiscountApplied(double discountApplied) { this.discountApplied = discountApplied; }
    public void setPayableAmount(double payableAmount) { this.payableAmount = payableAmount; }
    public void setLoyaltyPointsEarned(int loyaltyPointsEarned) { this.loyaltyPointsEarned = loyaltyPointsEarned; }
    public void setRewardNote(String rewardNote) { this.rewardNote = rewardNote; }

    public void addItem(BillItem item) {
        this.items.add(item);
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Bill#%d | Date: %s | CustomerId: %d | AdminId: %d | Gross: %.2f | Discount: %.2f | Payable: %.2f | Points: %d | %s",
                billNumber, billDate.format(df), customerId, billedByAdminId, grossAmount, discountApplied, payableAmount,
                loyaltyPointsEarned, rewardNote == null ? "" : rewardNote);
    }
}
