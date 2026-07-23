package com.bloom.rmi;

import java.io.Serializable;

/**
 * PaymentResult — returned by RMI PaymentService.processPayment()
 * Must be Serializable so it can be transmitted over RMI.
 */
public class PaymentResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String  transactionId;
    private String  message;
    private String  status;   // APPROVED | DECLINED | PENDING | ERROR

    // ── Constructors ─────────────────────────────────────
    public PaymentResult() {}

    public PaymentResult(boolean success, String transactionId,
                          String message, String status) {
        this.success       = success;
        this.transactionId = transactionId;
        this.message       = message;
        this.status        = status;
    }

    /** Factory: approved result */
    public static PaymentResult approved(String txId) {
        return new PaymentResult(true, txId, "Payment approved successfully.", "APPROVED");
    }

    /** Factory: declined result */
    public static PaymentResult declined(String reason) {
        return new PaymentResult(false, null, reason, "DECLINED");
    }

    /** Factory: error result */
    public static PaymentResult error(String errorMsg) {
        return new PaymentResult(false, null, errorMsg, "ERROR");
    }

    // ── Getters & Setters ─────────────────────────────────
    public boolean isSuccess()                   { return success; }
    public void setSuccess(boolean success)      { this.success = success; }

    public String getTransactionId()             { return transactionId; }
    public void setTransactionId(String txId)    { this.transactionId = txId; }

    public String getMessage()                   { return message; }
    public void setMessage(String message)       { this.message = message; }

    public String getStatus()                    { return status; }
    public void setStatus(String status)         { this.status = status; }

    @Override
    public String toString() {
        return "PaymentResult{success=" + success +
               ", txId=" + transactionId +
               ", status=" + status + "}";
    }
}
