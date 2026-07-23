package com.bloom.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

/**
 * PaymentServiceImpl — RMI Server Implementation
 *
 * Extends UnicastRemoteObject so it can be exported as an RMI object.
 *
 * Run PaymentServer.main() to bind this on the RMI registry (port 1099).
 * CheckoutServlet looks it up using:
 *   Naming.lookup("rmi://localhost/BloomPaymentService")
 */
public class PaymentServiceImpl extends UnicastRemoteObject
        implements PaymentService {

    private static final long serialVersionUID = 1L;

    public PaymentServiceImpl() throws RemoteException {
        super();
    }

    // ─────────────────────────────────────────────────────
    @Override
    public PaymentResult processPayment(int orderId, double amount,
                                         String method, String paymentToken)
            throws RemoteException {

        System.out.println("[RMI] processPayment called → orderId=" + orderId
                + ", amount=" + amount + ", method=" + method);

        // COD always succeeds
        if ("cod".equalsIgnoreCase(method)) {
            String txId = "COD-" + orderId + "-" + System.currentTimeMillis();
            return PaymentResult.approved(txId);
        }

        // Validate card if method = card
        if ("card".equalsIgnoreCase(method)) {
            if (paymentToken == null || paymentToken.length() < 12) {
                return PaymentResult.declined("Invalid card details.");
            }
            String cleaned = paymentToken.replaceAll("\\s+", "");
            if (!luhnCheck(cleaned)) {
                return PaymentResult.declined("Card validation failed (Luhn check).");
            }
        }

        // Validate UPI
        if ("upi".equalsIgnoreCase(method)) {
            if (!validateUPI(paymentToken)) {
                return PaymentResult.declined("Invalid UPI ID format.");
            }
        }

        // Simulate occasional random decline (5% chance) for realism
        if (Math.random() < 0.05) {
            return PaymentResult.declined("Transaction declined by bank. Please retry.");
        }

        // Generate unique transaction ID
        String txId = "BLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        System.out.println("[RMI] Payment APPROVED → txId=" + txId);
        return PaymentResult.approved(txId);
    }

    // ─────────────────────────────────────────────────────
    @Override
    public boolean validateCard(String cardNumber) throws RemoteException {
        if (cardNumber == null) return false;
        String cleaned = cardNumber.replaceAll("\\s+", "");
        if (!cleaned.matches("\\d{13,19}")) return false;
        return luhnCheck(cleaned);
    }

    // ─────────────────────────────────────────────────────
    @Override
    public boolean validateUPI(String upiId) throws RemoteException {
        if (upiId == null || upiId.trim().isEmpty()) return false;
        // UPI format: something@provider (e.g. name@okaxis, phone@upi)
        return upiId.matches("^[a-zA-Z0-9._-]+@[a-zA-Z]{2,}$");
    }

    // ─────────────────────────────────────────────────────
    @Override
    public boolean refund(String transactionId) throws RemoteException {
        System.out.println("[RMI] Refund initiated → txId=" + transactionId);
        // Simulate refund processing
        if (transactionId == null || transactionId.startsWith("COD")) return false;
        return true;
    }

    // ─────────────────────────────────────────────────────
    @Override
    public String ping() throws RemoteException {
        return "Bloom Payment RMI Service is alive ✅";
    }

    // ── Luhn Algorithm for Card Validation ───────────────
    /**
     * Luhn algorithm — industry standard card number checksum.
     * Used by all major card networks (Visa, Mastercard, etc.)
     */
    private boolean luhnCheck(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(String.valueOf(number.charAt(i)));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
