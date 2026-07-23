package com.bloom.model;

import javax.persistence.*;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Order entity — maps to 'orders' table
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> items;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "status", length = 20)
    private String status = "Pending";
    // Pending | Confirmed | Shipped | Delivered | Cancelled

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "Pending";

    @Column(name = "delivery_address", length = 400)
    private String deliveryAddress;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @PrePersist
    protected void onCreate() { createdAt = new java.util.Date(); }

    // ── Getters & Setters ──────────────────────────────────
    public int getOrderId()                     { return orderId; }
    public void setOrderId(int id)              { this.orderId = id; }

    public User getUser()                       { return user; }
    public void setUser(User user)              { this.user = user; }

    public List<CartItem> getItems()            { return items; }
    public void setItems(List<CartItem> items)  { this.items = items; }

    public double getTotalAmount()              { return totalAmount; }
    public void setTotalAmount(double amt)      { this.totalAmount = amt; }

    public String getStatus()                   { return status; }
    public void setStatus(String status)        { this.status = status; }

    public String getPaymentMethod()            { return paymentMethod; }
    public void setPaymentMethod(String pm)     { this.paymentMethod = pm; }

    public String getPaymentStatus()            { return paymentStatus; }
    public void setPaymentStatus(String ps)     { this.paymentStatus = ps; }

    public String getDeliveryAddress()          { return deliveryAddress; }
    public void setDeliveryAddress(String addr) { this.deliveryAddress = addr; }

    public java.util.Date getCreatedAt()        { return createdAt; }

    /** Formatted date for display in JSP */
    public String getFormattedDate() {
        if (createdAt == null) return "—";
        return new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(createdAt);
    }
}
