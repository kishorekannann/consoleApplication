package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;

    private double securityDeposit = 1500.0;
    private double fineLimit = 1000.0;
    private double outstandingFine = 0.0;

    public User(int id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    public double getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(double securityDeposit) { this.securityDeposit = securityDeposit; }

    public double getFineLimit() { return fineLimit; }
    public void setFineLimit(double fineLimit) { this.fineLimit = fineLimit; }

    public double getOutstandingFine() { return outstandingFine; }
    public void addOutstandingFine(double amount) { this.outstandingFine += amount; }
    public void clearOutstandingFine(double amount) { this.outstandingFine = Math.max(0, this.outstandingFine - amount); }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %s | Deposit: %.2f | OutstandingFine: %.2f | FineLimit: %.2f",
                id, name, email, role, securityDeposit, outstandingFine, fineLimit);
    }
}
