package shopeasy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart {

    private final List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        assert product != null : "product must not be null";
        assert quantity > 0 : "quantity must be > 0";

        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void updateQuantity(String productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found in cart: " + productId);
    }

    public double applyDiscount(double discountRate) {
        assert discountRate >= 0 && discountRate <= 100 : "discountRate must be between 0 and 100";
        
        double rawTotal = total();
        double discounted = rawTotal - (rawTotal * discountRate / 100);
        return discounted;
    }

    public double total() {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.subtotal();
        }
        return sum;
    }

    public int itemCount() {
        return items.size();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        return String.format("ShoppingCart{items=%d, total=%.2f}", items.size(), total());
    }
}