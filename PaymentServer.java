package com.bloom.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * PaymentServer — starts the RMI registry and binds the service.
 *
 * ── HOW TO RUN ──────────────────────────────────────────
 * Run this BEFORE starting your Tomcat server:
 *   javac com/bloom/rmi/*.java
 *   java com.bloom.rmi.PaymentServer
 *
 * It will print: "✅ Bloom RMI Payment Server started on port 1099"
 * ────────────────────────────────────────────────────────
 */
public class PaymentServer {

    private static final int    RMI_PORT    = 1099;
    private static final String SERVICE_URL = "rmi://localhost/BloomPaymentService";

    public static void main(String[] args) {
        try {
            // 1. Create RMI registry on port 1099
            LocateRegistry.createRegistry(RMI_PORT);
            System.out.println("✅ RMI Registry started on port " + RMI_PORT);

            // 2. Create the service implementation
            PaymentServiceImpl service = new PaymentServiceImpl();

            // 3. Bind to registry
            Naming.rebind(SERVICE_URL, service);

            System.out.println("✅ Bloom RMI Payment Server started");
            System.out.println("   Bound to: " + SERVICE_URL);
            System.out.println("   Waiting for client calls...\n");

            // Server stays alive
        } catch (Exception e) {
            System.err.println("❌ RMI Server startup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
