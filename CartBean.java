package com.bloom.bean;

import com.bloom.model.CartItem;
import com.bloom.model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * CartBean — JavaBean that manages the shopping cart.
 *
 * Stored in HttpSession as "cart".
 * Follows JavaBean conventions.
 */
public class CartBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private List<CartItem> items = new ArrayList<>();

    // ── Cart Operations ───────────────────────────────────

    /** Add a product to cart. If exists, increase quantity by 1. */
    public void addProduct(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId() == product.getProductId()) {
                // Check stock before increasing
                if (item.getQuantity() < product.getStock()) {
                    item.setQuantity(item.getQuantity() + 1);
                }
                return;
            }
        }
        // New item
        if (product.getStock() > 0) {
            items.add(new CartItem(product, 1));
        }
    }

    /** Remove a product from cart completely */
    public void removeProduct(int productId) {
        items.removeIf(item -> item.getProduct().getProductId() == productId);
    }

    /** Increase quantity of a specific product */
    public void increaseQuantity(int productId) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId() == productId) {
                if (item.getQuantity() < item.getProduct().getStock()) {
                    item.setQuantity(item.getQuantity() + 1);
                }
                return;
            }
        }
    }

    /** Decrease quantity; remove if reaches 0 */
    public void decreaseQuantity(int productId) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId() == productId) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    removeProduct(productId);
                }
                return;
            }
        }
    }

    /** Clear all items */
    public void clear() {
        items.clear();
    }

    /** Check if cart is empty */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Compatibility for JSP EL calls like cart.size() */
    public int size() {
        return items.size();
    }

    // ── Computed Properties ───────────────────────────────

    /** Total number of individual items */
    public int getTotalCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    /** Subtotal (before shipping and tax) */
    public double getSubtotal() {
        return items.stream()
                    .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                    .sum();
    }

    /** Shipping cost — free above ₹999 */
    public double getShipping() {
        return getSubtotal() >= 999 ? 0 : 99;
    }

    /** Tax at 5% */
    public double getTax() {
        return Math.round(getSubtotal() * 0.05 * 100.0) / 100.0;
    }

    /** Grand total */
    public double getTotal() {
        return Math.round((getSubtotal() + getShipping() + getTax()) * 100.0) / 100.0;
    }

    // ── Getters & Setters ─────────────────────────────────
    public List<CartItem> getItems()           { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}
