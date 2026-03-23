package data;

import model.Bill;
import model.Product;
import model.Role;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    public static final Map<Integer, Product> products = new HashMap<>();
    public static final Map<Integer, User> users = new HashMap<>();
    public static final List<Bill> bills = new ArrayList<>();

    public static int productSeq = 5;
    public static int userSeq = 4;
    public static int billSeq = 1000;

    static {
        users.put(1, new User(1, "Super Admin", "admin@mart.com", "admin123", Role.ADMIN, 0));
        users.put(2, new User(2, "Shift Admin", "admin2@mart.com", "admin123", Role.ADMIN, 1));

        User c1 = new User(3, "Arun", "c1@mart.com", "pass123", Role.CUSTOMER, 1);
        User c2 = new User(4, "Divya", "c2@mart.com", "pass123", Role.CUSTOMER, 2);
        users.put(3, c1);
        users.put(4, c2);

        products.put(1, new Product(1, "Rice 5kg", 420.0, 40));
        products.put(2, new Product(2, "Sunflower Oil 1L", 160.0, 55));
        products.put(3, new Product(3, "Milk 1L", 56.0, 80));
        products.put(4, new Product(4, "Eggs 12 Pack", 78.0, 35));
        products.put(5, new Product(5, "Sugar 1kg", 48.0, 60));
    }
}
