package com.bloom.dao;

import com.bloom.model.Order;
import com.bloom.model.CartItem;
import com.bloom.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

/**
 * OrderDAO — all DB operations for Order entity
 */
public class OrderDAO {

    /** Save a complete order with all cart items */
    public Order save(Order order) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Save order first to get ID
            session.save(order);

            // Link each cart item to this order and save
            if (order.getItems() != null) {
                for (CartItem item : order.getItems()) {
                    item.setOrder(order);
                    session.save(item);
                }
            }

            tx.commit();
            return order;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("OrderDAO.save error: " + e.getMessage());
            return null;
        }
    }

    /** Get all orders for a specific user */
    public List<Order> findByUser(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> q = session.createQuery(
                "FROM Order o WHERE o.user.userId = :uid ORDER BY o.createdAt DESC",
                Order.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    /** Get single order by ID */
    public Order findById(int orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Order.class, orderId);
        }
    }

    /** Get all orders (admin) */
    public List<Order> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Order o ORDER BY o.createdAt DESC", Order.class).list();
        }
    }

    /** Get recent N orders (for dashboard) */
    public List<Order> findRecent(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Order o ORDER BY o.createdAt DESC", Order.class)
                .setMaxResults(limit).list();
        }
    }

    /** Update order status */
    public boolean updateStatus(int orderId, String status) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Order order = session.get(Order.class, orderId);
            if (order == null) { tx.rollback(); return false; }
            order.setStatus(status);
            session.update(order);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    /** Count total orders */
    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Long) session.createQuery("SELECT COUNT(o) FROM Order o").uniqueResult();
        }
    }

    /** Calculate total revenue */
    public double getTotalRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Double rev = (Double) session.createQuery(
                "SELECT SUM(o.totalAmount) FROM Order o WHERE o.status != 'Cancelled'")
                .uniqueResult();
            return rev != null ? rev : 0.0;
        }
    }
}
