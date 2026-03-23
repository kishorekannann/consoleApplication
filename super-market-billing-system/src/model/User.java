package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;

    private double creditBalance;
    private int loyaltyPoints;
    private int pendingDiscountCoupons;
    private double totalSpent;
    private int createdByAdminId;

    public User(int id, String name, String email, String password, Role role, int createdByAdminId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdByAdminId = createdByAdminId;
        this.creditBalance = role == Role.CUSTOMER ? 1000.0 : 0.0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public double getCreditBalance() { return creditBalance; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public int getPendingDiscountCoupons() { return pendingDiscountCoupons; }
    public double getTotalSpent() { return totalSpent; }
    public int getCreatedByAdminId() { return createdByAdminId; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public void addCredit(double amount) {
        this.creditBalance += amount;
    }

    public boolean deductCredit(double amount) {
        if (amount > this.creditBalance) {
            return false;
        }
        this.creditBalance -= amount;
        return true;
    }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
        while (this.loyaltyPoints >= 50) {
            this.loyaltyPoints -= 50;
            this.pendingDiscountCoupons += 1;
        }
    }

    public double consumeOneDiscountCoupon() {
        if (pendingDiscountCoupons <= 0) {
            return 0.0;
        }
        pendingDiscountCoupons -= 1;
        return 100.0;
    }

    public void addSpent(double amount) {
        this.totalSpent += amount;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %s | Credit: %.2f | Points: %d | Coupons: %d | Spent: %.2f",
                id, name, email, role, creditBalance, loyaltyPoints, pendingDiscountCoupons, totalSpent);
    }
}
