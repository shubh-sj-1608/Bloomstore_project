package com.bloom.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * HibernateUtil — singleton SessionFactory
 * Used by all DAOs to get Hibernate sessions.
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Reads hibernate.cfg.xml from WEB-INF/classes
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("❌ Hibernate SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /** Returns the singleton SessionFactory */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /** Closes the SessionFactory (call on app shutdown) */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
