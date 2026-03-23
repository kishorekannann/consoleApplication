package service;

import data.DataStore;
import model.Product;
import model.Role;
import model.User;
import repository.ProductRepository;
import repository.UserRepository;

import java.util.List;

public class InventoryService {
    private final ProductRepository productRepository = new ProductRepository();
    private final UserRepository userRepository = new UserRepository();

    public Product addProduct(String name, double price, int quantity) {
        Product product = new Product(++DataStore.productSeq, name, price, quantity);
        productRepository.save(product);
        return product;
    }

    public boolean modifyProduct(int productId, String name, double price, int quantity) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            return false;
        }
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        return true;
    }

    public boolean deleteProduct(int productId) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            return false;
        }
        productRepository.delete(productId);
        return true;
    }

    public List<Product> listProductsByName() {
        return productRepository.findAllSortedByName();
    }

    public List<Product> listProductsByPrice() {
        return productRepository.findAllSortedByPrice();
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContains(keyword);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    public User addUser(String name, String email, String password, Role role, int createdByAdminId) {
        if (userRepository.findByEmail(email) != null) {
            return null;
        }
        User user = new User(++DataStore.userSeq, name, email, password, role, createdByAdminId);
        userRepository.save(user);
        return user;
    }

    public boolean increaseCustomerCredit(int customerId, double amount) {
        User user = userRepository.findById(customerId);
        if (user == null || user.getRole() != Role.CUSTOMER || amount <= 0) {
            return false;
        }
        user.addCredit(amount);
        return true;
    }
}
