package repository;

import data.DataStore;
import model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {
    public List<Product> findAll() {
        return new ArrayList<>(DataStore.products.values());
    }

    public Product findById(int id) {
        return DataStore.products.get(id);
    }

    public List<Product> findByNameContains(String keyword) {
        String k = keyword.toLowerCase();
        return DataStore.products.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public List<Product> findAllSortedByName() {
        return findAll().stream()
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Product> findAllSortedByPrice() {
        return findAll().stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .collect(Collectors.toList());
    }

    public void save(Product product) {
        DataStore.products.put(product.getId(), product);
    }

    public void delete(int productId) {
        DataStore.products.remove(productId);
    }
}
