package ui;

import model.Bill;
import model.BillItem;
import model.CartItem;
import model.Product;
import model.Role;
import model.User;
import repository.UserRepository;
import service.AuthService;
import service.BillingService;
import service.CartService;
import service.InventoryService;
import service.ReportService;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);

    private static final AuthService authService = new AuthService();
    private static final InventoryService inventoryService = new InventoryService();
    private static final CartService cartService = new CartService();
    private static final BillingService billingService = new BillingService();
    private static final ReportService reportService = new ReportService();
    private static final UserRepository userRepository = new UserRepository();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Super Market Billing System ===");
            String email = readLine("Email: ");
            String password = readLine("Password: ");

            User user = authService.login(email, password);
            if (user == null) {
                System.out.println("Invalid credentials.");
                continue;
            }

            System.out.println("Welcome " + user.getName() + " (" + user.getRole() + ")");
            if (user.getRole() == Role.ADMIN) {
                adminMenu(user);
            } else {
                customerMenu(user);
            }
        }
    }

    private static void adminMenu(User admin) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Product");
            System.out.println("2. Modify Product");
            System.out.println("3. Delete Product");
            System.out.println("4. List Products By Name");
            System.out.println("5. List Products By Price");
            System.out.println("6. Search Product By Name");
            System.out.println("7. Add Admin/Customer");
            System.out.println("8. Increase Customer Credit");
            System.out.println("9. Reports");
            System.out.println("0. Logout");

            int choice = readInt("Choose option: ");
            if (choice == 0) {
                return;
            }

            switch (choice) {
                case 1:
                    addProductFlow();
                    break;
                case 2:
                    modifyProductFlow();
                    break;
                case 3:
                    deleteProductFlow();
                    break;
                case 4:
                    inventoryService.listProductsByName().forEach(System.out::println);
                    break;
                case 5:
                    inventoryService.listProductsByPrice().forEach(System.out::println);
                    break;
                case 6:
                    searchProductFlow();
                    break;
                case 7:
                    addUserFlow(admin);
                    break;
                case 8:
                    increaseCustomerCreditFlow();
                    break;
                case 9:
                    reportsFlow();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void customerMenu(User customer) {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("Credit: " + String.format("%.2f", customer.getCreditBalance())
                    + " | Points: " + customer.getLoyaltyPoints()
                    + " | Discount Coupons: " + customer.getPendingDiscountCoupons());
            System.out.println("1. View All Products");
            System.out.println("2. Add Product To Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Edit Cart Product Quantity");
            System.out.println("5. Delete Product From Cart");
            System.out.println("6. Proceed To Payment");
            System.out.println("7. Purchase History");
            System.out.println("0. Logout");

            int choice = readInt("Choose option: ");
            if (choice == 0) {
                return;
            }

            switch (choice) {
                case 1:
                    inventoryService.listProductsByName().forEach(System.out::println);
                    break;
                case 2:
                    addToCartFlow(customer);
                    break;
                case 3:
                    viewCart(customer);
                    break;
                case 4:
                    editCartFlow(customer);
                    break;
                case 5:
                    removeCartItemFlow(customer);
                    break;
                case 6:
                    checkoutFlow(customer);
                    break;
                case 7:
                    reportService.customerPurchaseHistory(customer.getId()).forEach(System.out::println);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void addProductFlow() {
        String name = readLine("Product Name: ");
        double price = readDouble("Price: ");
        int qty = readInt("Quantity: ");
        Product product = inventoryService.addProduct(name, price, qty);
        System.out.println("Added product: " + product);
    }

    private static void modifyProductFlow() {
        int id = readInt("Product ID: ");
        Product existing = inventoryService.getProductById(id);
        if (existing == null) {
            System.out.println("Product not found.");
            return;
        }

        String name = readLine("New Name: ");
        double price = readDouble("New Price: ");
        int qty = readInt("New Quantity: ");
        boolean updated = inventoryService.modifyProduct(id, name, price, qty);
        System.out.println(updated ? "Product modified." : "Unable to modify product.");
    }

    private static void deleteProductFlow() {
        int id = readInt("Product ID to delete: ");
        boolean deleted = inventoryService.deleteProduct(id);
        System.out.println(deleted ? "Product deleted." : "Product not found.");
    }

    private static void searchProductFlow() {
        String keyword = readLine("Search keyword: ");
        List<Product> products = inventoryService.searchProducts(keyword);
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        products.forEach(System.out::println);
    }

    private static void addUserFlow(User admin) {
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        String password = readLine("Password: ");
        String roleInput = readLine("Role (ADMIN/CUSTOMER): ").trim().toUpperCase();

        Role role;
        try {
            role = Role.valueOf(roleInput);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid role.");
            return;
        }

        User user = inventoryService.addUser(name, email, password, role, admin.getId());
        if (user == null) {
            System.out.println("Email already exists.");
            return;
        }
        System.out.println("Added user: " + user);
    }

    private static void increaseCustomerCreditFlow() {
        int customerId = readInt("Customer ID: ");
        double amount = readDouble("Amount to add: ");
        boolean ok = inventoryService.increaseCustomerCredit(customerId, amount);
        System.out.println(ok ? "Credit updated." : "Invalid customer or amount.");
    }

    private static void reportsFlow() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Products with less quantity");
        System.out.println("2. Products never bought");
        System.out.println("3. Customers by purchase value");
        System.out.println("4. Admins by sales value");

        int choice = readInt("Choose report: ");
        switch (choice) {
            case 1:
                int threshold = readInt("Threshold: ");
                reportService.productsWithLessQuantity(threshold).forEach(System.out::println);
                break;
            case 2:
                reportService.productsNeverBought().forEach(System.out::println);
                break;
            case 3:
                reportService.topCustomersByPurchaseValue().forEach(System.out::println);
                break;
            case 4:
                reportService.adminsBySalesValue().forEach(System.out::println);
                break;
            default:
                System.out.println("Invalid report option.");
        }
    }

    private static void addToCartFlow(User customer) {
        int productId = readInt("Product ID: ");
        Product product = inventoryService.getProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }
        int quantity = readInt("Quantity: ");
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }
        cartService.addToCart(customer.getId(), productId, quantity);
        System.out.println("Added to cart.");
    }

    private static void viewCart(User customer) {
        Collection<CartItem> items = cartService.getCartItems(customer.getId());
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        double estTotal = 0.0;
        for (CartItem item : items) {
            Product p = inventoryService.getProductById(item.getProductId());
            if (p == null) {
                continue;
            }
            double line = p.getPrice() * item.getQuantity();
            estTotal += line;
            System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPrice() + " x " + item.getQuantity() + " = " + line);
        }
        System.out.println("Estimated Total: " + String.format("%.2f", estTotal));
    }

    private static void editCartFlow(User customer) {
        int productId = readInt("Product ID: ");
        int qty = readInt("New quantity (0 to remove): ");
        cartService.updateQuantity(customer.getId(), productId, qty);
        System.out.println("Cart updated.");
    }

    private static void removeCartItemFlow(User customer) {
        int productId = readInt("Product ID to remove: ");
        cartService.removeFromCart(customer.getId(), productId);
        System.out.println("Removed from cart.");
    }

    private static void checkoutFlow(User customer) {
        Collection<CartItem> items = cartService.getCartItems(customer.getId());
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        int suggestedAdmin = customer.getCreatedByAdminId() > 0 ? customer.getCreatedByAdminId() : 1;
        System.out.println("Billing Admin IDs available:");
        userRepository.findByRole(Role.ADMIN).forEach(System.out::println);
        int adminId = readInt("Enter billing Admin ID (suggested " + suggestedAdmin + "): ");

        BillingService.CheckoutResult result = billingService.checkout(customer.getId(), adminId, items);
        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return;
        }

        cartService.clearCart(customer.getId());
        Bill bill = result.getBill();
        System.out.println(result.getMessage());
        System.out.println(bill);
        for (BillItem item : bill.getItems()) {
            System.out.println("  " + item);
        }
        System.out.println("Remaining Credit: " + String.format("%.2f", customer.getCreditBalance()));
    }

    private static String readLine(String label) {
        System.out.print(label);
        if (!sc.hasNextLine()) {
            System.out.println("\nExiting application.");
            System.exit(0);
        }
        return sc.nextLine();
    }

    private static int readInt(String label) {
        while (true) {
            try {
                return Integer.parseInt(readLine(label));
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private static double readDouble(String label) {
        while (true) {
            try {
                return Double.parseDouble(readLine(label));
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid number.");
            }
        }
    }
}
