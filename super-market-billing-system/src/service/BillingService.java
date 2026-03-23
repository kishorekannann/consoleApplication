package service;

import data.DataStore;
import model.Bill;
import model.BillItem;
import model.CartItem;
import model.Product;
import model.Role;
import model.User;
import repository.BillRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BillingService {
    private final ProductRepository productRepository = new ProductRepository();
    private final UserRepository userRepository = new UserRepository();
    private final BillRepository billRepository = new BillRepository();

    public static class CheckoutResult {
        private final boolean success;
        private final String message;
        private final Bill bill;

        public CheckoutResult(boolean success, String message, Bill bill) {
            this.success = success;
            this.message = message;
            this.bill = bill;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Bill getBill() { return bill; }
    }

    public CheckoutResult checkout(int customerId, int billedByAdminId, Collection<CartItem> cartItems) {
        User customer = userRepository.findById(customerId);
        User admin = userRepository.findById(billedByAdminId);

        if (customer == null || customer.getRole() != Role.CUSTOMER) {
            return new CheckoutResult(false, "Invalid customer.", null);
        }
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return new CheckoutResult(false, "Invalid billing admin.", null);
        }
        if (cartItems == null || cartItems.isEmpty()) {
            return new CheckoutResult(false, "Cart is empty.", null);
        }

        List<CartItem> items = new ArrayList<>(cartItems);
        double gross = 0.0;

        for (CartItem item : items) {
            Product p = productRepository.findById(item.getProductId());
            if (p == null) {
                return new CheckoutResult(false, "Product not found: " + item.getProductId(), null);
            }
            if (item.getQuantity() <= 0) {
                return new CheckoutResult(false, "Invalid quantity for product: " + item.getProductId(), null);
            }
            if (p.getQuantity() < item.getQuantity()) {
                return new CheckoutResult(false, "Insufficient stock for: " + p.getName(), null);
            }
            gross += p.getPrice() * item.getQuantity();
        }

        double discount = customer.consumeOneDiscountCoupon();
        if (discount > gross) {
            discount = gross;
        }
        double payable = gross - discount;
        if (customer.getCreditBalance() < payable) {
            return new CheckoutResult(false, "Credit limit exceeded. Available credit: " + customer.getCreditBalance(), null);
        }

        Bill bill = new Bill(++DataStore.billSeq, customer.getId(), admin.getId(), LocalDateTime.now());
        for (CartItem item : items) {
            Product p = productRepository.findById(item.getProductId());
            p.reduceQuantity(item.getQuantity());
            bill.addItem(new BillItem(p.getId(), p.getName(), p.getPrice(), item.getQuantity()));
        }

        customer.deductCredit(payable);
        customer.addSpent(payable);

        int pointsEarned = 0;
        String rewardNote;
        if (payable >= 5000) {
            customer.addCredit(100);
            rewardNote = "Payback: Rs 100 added to wallet (bill >= 5000), no points awarded.";
        } else {
            pointsEarned = (int) (payable / 100);
            customer.addLoyaltyPoints(pointsEarned);
            rewardNote = "Loyalty points added: " + pointsEarned;
        }

        bill.setGrossAmount(gross);
        bill.setDiscountApplied(discount);
        bill.setPayableAmount(payable);
        bill.setLoyaltyPointsEarned(pointsEarned);
        bill.setRewardNote(rewardNote);

        billRepository.save(bill);
        return new CheckoutResult(true, "Checkout successful.", bill);
    }
}
