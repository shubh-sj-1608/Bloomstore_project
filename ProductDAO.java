package com.bloom.dao;

import com.bloom.model.Product;
import com.bloom.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

/**
 * ProductDAO — all DB operations for Product entity
 */
public class ProductDAO {

    /** Save a new product */
    public boolean save(Product product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(product);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("ProductDAO.save error: " + e.getMessage());
            return false;
        }
    }

    /** Get all products */
    public List<Product> findAll() {
        ensureProductImages();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery(
                "FROM Product ORDER BY createdAt DESC", Product.class).list();
            if (products.isEmpty()) {
                seedDefaultProducts();
                return session.createQuery(
                    "FROM Product ORDER BY createdAt DESC", Product.class).list();
            }
            return products;
        }
    }

    /** Get products by category */
    public List<Product> findByCategory(String category) {
        seedDefaultProducts();
        ensureProductImages();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> q = session.createQuery(
                "FROM Product WHERE category = :cat ORDER BY name", Product.class);
            q.setParameter("cat", category);
            return q.list();
        }
    }

    /** Search by name or description */
    public List<Product> search(String keyword) {
        seedDefaultProducts();
        ensureProductImages();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> q = session.createQuery(
                "FROM Product WHERE LOWER(name) LIKE :kw OR LOWER(description) LIKE :kw",
                Product.class);
            q.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            return q.list();
        }
    }

    /** Get featured products for homepage */
    public List<Product> findFeatured() {
        ensureProductImages();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery(
                "FROM Product WHERE featured = true AND stock > 0", Product.class)
                .setMaxResults(8).list();
            if (products.isEmpty()) {
                seedDefaultProducts();
                return session.createQuery(
                    "FROM Product WHERE featured = true AND stock > 0", Product.class)
                    .setMaxResults(8).list();
            }
            return products;
        }
    }

    /** Get latest products for the New Arrivals filter */
    public List<Product> findRecent(int limit) {
        seedDefaultProducts();
        ensureProductImages();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Product WHERE stock > 0 ORDER BY createdAt DESC", Product.class)
                .setMaxResults(limit).list();
        }
    }

    /** Find by ID */
    public Product findById(int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Product.class, productId);
        }
    }

    /** Update product (stock, price etc.) */
    public boolean update(Product product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(product);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    /** Decrease stock after purchase */
    public boolean decreaseStock(int productId, int qty) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product p = session.get(Product.class, productId);
            if (p == null || p.getStock() < qty) {
                tx.rollback();
                return false;
            }
            p.setStock(p.getStock() - qty);
            session.update(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    /** Delete product */
    public boolean delete(int productId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product p = session.get(Product.class, productId);
            if (p != null) session.delete(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    /** Count total products */
    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Long) session.createQuery("SELECT COUNT(p) FROM Product p").uniqueResult();
        }
    }

    /** Get low-stock products (stock < 5) */
    public List<Product> findLowStock() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Product WHERE stock < 5 AND stock > 0 ORDER BY stock", Product.class).list();
        }
    }

    private void seedDefaultProducts() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = (Long) session.createQuery("SELECT COUNT(p) FROM Product p").uniqueResult();
            if (count != null && count > 0) {
                return;
            }

            tx = session.beginTransaction();
            saveSeed(session, "Monstera Deliciosa", "The classic split-leaf plant. Easy to care for, stunning indoors.", 799.00, 30, "Plants", true, imageFor("Monstera Deliciosa"));
            saveSeed(session, "Peace Lily", "Elegant white blooms. Thrives in low light. Air-purifying.", 549.00, 25, "Plants", true, imageFor("Peace Lily"));
            saveSeed(session, "Snake Plant", "Near-indestructible. Perfect for beginners. Filters air at night.", 449.00, 40, "Plants", true, imageFor("Snake Plant"));
            saveSeed(session, "Pothos Golden", "Trailing vines with golden-green leaves. Extremely low maintenance.", 299.00, 50, "Plants", true, imageFor("Pothos Golden"));
            saveSeed(session, "Areca Palm", "Feathery indoor palm that brightens living rooms and balconies.", 899.00, 18, "Plants", true, imageFor("Areca Palm"));
            saveSeed(session, "Calathea Orbifolia", "Large striped leaves with a soft tropical look for shaded rooms.", 749.00, 14, "Plants", true, imageFor("Calathea Orbifolia"));
            saveSeed(session, "Terracotta Classic Pot", "Handmade unglazed terracotta. Breathable for roots.", 349.00, 60, "Pots", true, imageFor("Terracotta Classic Pot"));
            saveSeed(session, "Hanging Macrame Planter", "Cotton macrame hanger with a compact planter for trailing vines.", 449.00, 32, "Pots", true, imageFor("Hanging Macrame Planter"));
            saveSeed(session, "Self-Watering Pot", "Modern planter with a water reservoir for easy plant care.", 649.00, 24, "Pots", false, imageFor("Self-Watering Pot"));
            saveSeed(session, "Organic Liquid Fertiliser", "All-purpose NPK blend. Safe for indoors. 500ml bottle.", 199.00, 80, "Care", true, imageFor("Organic Liquid Fertiliser"));
            saveSeed(session, "Neem Oil Spray", "Natural pest control. Cold-pressed neem. 250ml.", 149.00, 90, "Care", true, imageFor("Neem Oil Spray"));
            saveSeed(session, "Mini Gardening Tool Set", "Small rake, trowel, and pruning snip set for indoor gardening.", 399.00, 28, "Care", true, imageFor("Mini Gardening Tool Set"));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("ProductDAO.seedDefaultProducts error: " + e.getMessage());
        }
    }

    private void saveSeed(Session session, String name, String description, double price,
                          int stock, String category, boolean featured, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setFeatured(featured);
        product.setImageUrl(imageUrl);
        session.save(product);
    }

    private void ensureProductImages() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery(
                "FROM Product WHERE imageUrl IS NULL OR imageUrl = ''", Product.class).list();
            if (products.isEmpty()) {
                return;
            }

            tx = session.beginTransaction();
            for (Product product : products) {
                product.setImageUrl(imageFor(product.getName()));
                session.update(product);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("ProductDAO.ensureProductImages error: " + e.getMessage());
        }
    }

    private String imageFor(String name) {
        if (name == null) return imageForCategory("Plants");
        switch (name) {
            case "Monstera Deliciosa":
                return "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?auto=format&fit=crop&w=800&q=80";
            case "Peace Lily":
                return "https://images.unsplash.com/photo-1598880940080-ff9a29891b85?auto=format&fit=crop&w=800&q=80";
            case "Snake Plant":
                return "https://images.unsplash.com/photo-1593691509543-c55fb32d8de5?auto=format&fit=crop&w=800&q=80";
            case "Pothos Golden":
                return "https://images.unsplash.com/photo-1622398925373-3f91b1e275f5?auto=format&fit=crop&w=800&q=80";
            case "Areca Palm":
                return "https://images.unsplash.com/photo-1604762524889-3e2fcc145683?auto=format&fit=crop&w=800&q=80";
            case "Calathea Orbifolia":
                return "https://images.unsplash.com/photo-1616500285636-f5dca1191dbe?auto=format&fit=crop&w=800&q=80";
            case "Terracotta Classic Pot":
                return "https://images.unsplash.com/photo-1485955900006-10f4d324d411?auto=format&fit=crop&w=800&q=80";
            case "Hanging Macrame Planter":
                return "https://images.unsplash.com/photo-1602923668104-8f483c6e8f82?auto=format&fit=crop&w=800&q=80";
            case "Self-Watering Pot":
            case "Ceramic Matte White":
            case "Woven Rattan Basket":
            case "Cement Cylinder Pot":
                return "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=800&q=80";
            case "Organic Liquid Fertiliser":
            case "Neem Oil Spray":
            case "Leaf Shine Spray":
                return "https://images.unsplash.com/photo-1611073061165-68a6f8080f29?auto=format&fit=crop&w=800&q=80";
            case "Mini Gardening Tool Set":
                return "https://images.unsplash.com/photo-1416879595882-3373a0480b5b?auto=format&fit=crop&w=800&q=80";
            case "Well-Draining Potting Mix":
            case "Pebble Tray Set":
                return "https://images.unsplash.com/photo-1459156212016-c812468e2115?auto=format&fit=crop&w=800&q=80";
            default:
                return imageForCategory("Plants");
        }
    }

    private String imageForCategory(String category) {
        if ("Pots".equals(category)) {
            return "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=800&q=80";
        }
        if ("Care".equals(category)) {
            return "https://images.unsplash.com/photo-1416879595882-3373a0480b5b?auto=format&fit=crop&w=800&q=80";
        }
        return "https://images.unsplash.com/photo-1463320726281-696a485928c7?auto=format&fit=crop&w=800&q=80";
    }
}
