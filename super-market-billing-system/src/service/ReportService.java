package service;

import model.Bill;
import model.Product;
import model.Role;
import model.User;
import repository.BillRepository;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
    private final ProductRepository productRepository = new ProductRepository();
    private final UserRepository userRepository = new UserRepository();
    private final BillRepository billRepository = new BillRepository();

    public List<Product> productsWithLessQuantity(int threshold) {
        return productRepository.findAll().stream()
                .filter(p -> p.getQuantity() < threshold)
                .sorted(Comparator.comparingInt(Product::getQuantity))
                .collect(Collectors.toList());
    }

    public List<Product> productsNeverBought() {
        return productRepository.findAll().stream()
                .filter(p -> p.getSoldQuantity() == 0)
                .collect(Collectors.toList());
    }

    public List<String> topCustomersByPurchaseValue() {
        return userRepository.findByRole(Role.CUSTOMER).stream()
                .sorted(Comparator.comparingDouble(User::getTotalSpent).reversed())
                .map(u -> String.format("%d | %s | %.2f", u.getId(), u.getName(), u.getTotalSpent()))
                .collect(Collectors.toList());
    }

    public List<String> adminsBySalesValue() {
        Map<Integer, Double> salesByAdmin = billRepository.findAll().stream()
                .collect(Collectors.groupingBy(Bill::getBilledByAdminId,
                        Collectors.summingDouble(Bill::getPayableAmount)));

        return userRepository.findByRole(Role.ADMIN).stream()
                .map(a -> String.format("%d | %s | %.2f", a.getId(), a.getName(), salesByAdmin.getOrDefault(a.getId(), 0.0)))
                .sorted((a, b) -> {
                    double av = Double.parseDouble(a.substring(a.lastIndexOf('|') + 1).trim());
                    double bv = Double.parseDouble(b.substring(b.lastIndexOf('|') + 1).trim());
                    return Double.compare(bv, av);
                })
                .collect(Collectors.toList());
    }

    public List<Bill> customerPurchaseHistory(int customerId) {
        return billRepository.findByCustomerId(customerId).stream()
                .sorted(Comparator.comparing(Bill::getBillDate).reversed())
                .collect(Collectors.toList());
    }
}
