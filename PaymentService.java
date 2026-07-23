package com.bloom.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * PaymentService — RMI Remote Interface
 *
 * This interface defines the contract for the distributed
 * payment service. The client (CheckoutServlet) calls these
 * methods remotely via Java RMI.
 *
 * Demonstrates: Distributed Layer requirement of the project.
 */
public interface PaymentService extends Remote {

    /**
     * Process a payment transaction.
     *
     * @param orderId       Unique order reference
     * @param amount        Total amount to charge
     * @param method        Payment method (card, upi, netbanking, cod)
     * @param paymentToken  Card/UPI token for validation
     * @return PaymentResult with success status and transaction ID
     */
    PaymentResult processPayment(int orderId, double amount,
                                  String method, String paymentToken)
            throws RemoteException;

    /**
     * Validate a card number using Luhn algorithm.
     *
     * @param cardNumber Card number (digits only, no spaces)
     * @return true if valid, false otherwise
     */
    boolean validateCard(String cardNumber) throws RemoteException;

    /**
     * Validate a UPI ID format.
     *
     * @param upiId UPI ID string (e.g. name@upi)
     * @return true if format is valid
     */
    boolean validateUPI(String upiId) throws RemoteException;

    /**
     * Refund a transaction (for cancelled orders).
     *
     * @param transactionId The original transaction ID
     * @return true if refund initiated successfully
     */
    boolean refund(String transactionId) throws RemoteException;

    /**
     * Check if the payment service is alive (health check).
     */
    String ping() throws RemoteException;
}
