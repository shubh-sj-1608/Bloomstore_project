package com.bloom.dao;

import com.bloom.model.User;
import com.bloom.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

/**
 * UserDAO — all DB operations for User entity
 */
public class UserDAO {

    /** Save a new user */
    public boolean save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("UserDAO.save error: " + e.getMessage());
            return false;
        }
    }

    /** Find user by email (for login) */
    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                "FROM User WHERE LOWER(email) = :email", User.class);
            query.setParameter("email", email == null ? null : email.trim().toLowerCase());
            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("UserDAO.findByEmail error: " + e.getMessage());
            return null;
        }
    }

    /** Find user by ID */
    public User findById(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, userId);
        }
    }

    /** Check if email already exists */
    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    /** Get all users (admin) */
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User ORDER BY createdAt DESC", User.class).list();
        }
    }

    /** Count total users */
    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Long) session.createQuery("SELECT COUNT(u) FROM User u").uniqueResult();
        }
    }

    /** Update user */
    public boolean update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    /** Delete user */
    public boolean delete(int userId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) session.delete(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }
}
