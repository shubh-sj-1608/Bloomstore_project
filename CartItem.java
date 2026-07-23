package com.bloom.model;

import javax.persistence.*;

/**
 * CartItem entity — represents one product line in an Order
 * Maps to 'cart_items' table
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private int cartItemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;  // snapshot of price at time of order

    // ── Constructors ─────────────────────────────────────
    public CartItem() {}

    public CartItem(Product product, int quantity) {
        this.product   = product;
        this.quantity  = quantity;
        this.unitPrice = product.getPrice();
    }

    // ── Getters & Setters ─────────────────────────────────
    public int getCartItemId()              { return cartItemId; }
    public void setCartItemId(int id)       { this.cartItemId = id; }

    public Order getOrder()                 { return order; }
    public void setOrder(Order order)       { this.order = order; }

    public Product getProduct()             { return product; }
    public void setProduct(Product p)       { this.product = p; }

    public int getQuantity()                { return quantity; }
    public void setQuantity(int qty)        { this.quantity = qty; }

    public double getUnitPrice()            { return unitPrice; }
    public void setUnitPrice(double price)  { this.unitPrice = price; }

    /** Computed subtotal for this line */
    public double getSubtotal() {
        return unitPrice * quantity;
    }
}
