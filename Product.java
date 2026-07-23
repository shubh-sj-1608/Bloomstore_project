package com.bloom.model;

import javax.persistence.*;

/**
 * Product entity — maps to 'products' table
 */
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "category", length = 60)
    private String category;   // Plants | Pots | Care

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    @Column(name = "is_featured")
    private boolean featured = false;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @PrePersist
    protected void onCreate() { createdAt = new java.util.Date(); }

    // ── Getters & Setters ──────────────────────────────────
    public int getProductId()               { return productId; }
    public void setProductId(int id)        { this.productId = id; }

    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    public String getDescription()          { return description; }
    public void setDescription(String d)    { this.description = d; }

    public double getPrice()                { return price; }
    public void setPrice(double price)      { this.price = price; }

    public int getStock()                   { return stock; }
    public void setStock(int stock)         { this.stock = stock; }

    public String getCategory()             { return category; }
    public void setCategory(String cat)     { this.category = cat; }

    public String getImageUrl()             { return imageUrl; }
    public void setImageUrl(String url)     { this.imageUrl = url; }

    public boolean isFeatured()             { return featured; }
    public void setFeatured(boolean f)      { this.featured = f; }

    public java.util.Date getCreatedAt()    { return createdAt; }

    @Override
    public String toString() {
        return "Product{id=" + productId + ", name=" + name + ", price=" + price + "}";
    }
}
