package service;

import model.CartItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CartService {
    private final Map<Integer, Map<Integer, CartItem>> carts = new HashMap<>();

    public void addToCart(int customerId, int productId, int quantity) {
        Map<Integer, CartItem> cart = carts.computeIfAbsent(customerId, c -> new HashMap<>());
        CartItem item = cart.get(productId);
        if (item == null) {
            cart.put(productId, new CartItem(productId, quantity));
        } else {
            item.increaseQuantity(quantity);
        }
    }

    public void updateQuantity(int customerId, int productId, int quantity) {
        Map<Integer, CartItem> cart = carts.computeIfAbsent(customerId, c -> new HashMap<>());
        CartItem item = cart.get(productId);
        if (item == null) {
            return;
        }
        if (quantity <= 0) {
            cart.remove(productId);
            return;
        }
        item.setQuantity(quantity);
    }

    public void removeFromCart(int customerId, int productId) {
        Map<Integer, CartItem> cart = carts.computeIfAbsent(customerId, c -> new HashMap<>());
        cart.remove(productId);
    }

    public Collection<CartItem> getCartItems(int customerId) {
        return carts.computeIfAbsent(customerId, c -> new HashMap<>()).values();
    }

    public void clearCart(int customerId) {
        carts.computeIfAbsent(customerId, c -> new HashMap<>()).clear();
    }
}
