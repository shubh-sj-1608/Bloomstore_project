<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.lastOrder}"><c:redirect url="index.jsp"/></c:if>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Order Confirmed · Bloom</title>
  <link rel="stylesheet" href="css/bloom.css"/>
  <style>
    .success-page {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 40px 20px;
      background: var(--parchment);
    }
    .success-card {
      background: white;
      max-width: 560px;
      width: 100%;
      border-radius: 20px;
      padding: 52px 48px;
      text-align: center;
      box-shadow: 0 12px 60px rgba(0,0,0,0.07);
    }
    .success-icon {
      width: 80px; height: 80px;
      background: linear-gradient(135deg, var(--sage-light), #a8d5a2);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 2.4rem;
      margin: 0 auto 28px;
      animation: popIn 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    }
    @keyframes popIn {
      from { transform: scale(0); opacity: 0; }
      to   { transform: scale(1); opacity: 1; }
    }
    .success-title {
      font-family: var(--font-serif);
      font-size: 2.4rem;
      font-weight: 300;
      color: var(--ink);
      margin-bottom: 10px;
    }
    .success-sub {
      font-size: 0.9rem;
      color: var(--muted);
      margin-bottom: 32px;
      line-height: 1.7;
    }
    .order-details {
      background: var(--cream);
      border-radius: 12px;
      padding: 22px;
      margin-bottom: 32px;
      text-align: left;
    }
    .od-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 0;
      font-size: 0.88rem;
      border-bottom: 1px solid var(--border);
    }
    .od-row:last-child { border-bottom: none; }
    .od-label { color: var(--muted); }
    .od-value { font-weight: 500; color: var(--charcoal); }
    .od-value.highlight {
      font-family: var(--font-serif);
      font-size: 1.1rem;
      color: var(--terracotta);
    }
    .tx-id {
      font-family: monospace;
      background: var(--sage-light);
      color: #1F4E2A;
      padding: 3px 10px;
      border-radius: 6px;
      font-size: 0.82rem;
      letter-spacing: 0.05em;
    }
  </style>
</head>
<body>
<div class="success-page">
  <div class="success-card">

    <div class="success-icon">🌿</div>

    <h1 class="success-title">Order Confirmed!</h1>
    <p class="success-sub">
      Thank you, ${sessionScope.user.firstName}! Your plants are on their way.<br/>
      You'll receive a confirmation soon.
    </p>

    <div class="order-details">
      <div class="od-row">
        <span class="od-label">Order ID</span>
        <span class="od-value highlight">#${sessionScope.lastOrder.orderId}</span>
      </div>
      <div class="od-row">
        <span class="od-label">Total Paid</span>
        <span class="od-value">₹${sessionScope.lastOrder.totalAmount}</span>
      </div>
      <div class="od-row">
        <span class="od-label">Payment Method</span>
        <span class="od-value">${sessionScope.lastOrder.paymentMethod}</span>
      </div>
      <div class="od-row">
        <span class="od-label">Transaction ID</span>
        <span class="od-value tx-id">${sessionScope.lastTransactionId}</span>
      </div>
      <div class="od-row">
        <span class="od-label">Status</span>
        <span class="status-badge status-confirmed">${sessionScope.lastOrder.status}</span>
      </div>
      <div class="od-row">
        <span class="od-label">Payment Gateway</span>
        <span class="rmi-badge">🔒 RMI Secured</span>
      </div>
    </div>

    <div style="display:flex;gap:12px;justify-content:center;flex-wrap:wrap;">
      <a href="orders.jsp" class="btn btn-primary">View My Orders</a>
      <a href="products.jsp" class="btn btn-outline">Continue Shopping</a>
    </div>

    <%-- Clear lastOrder from session after display --%>
    <% session.removeAttribute("lastOrder"); session.removeAttribute("lastTransactionId"); %>
  </div>
</div>
</body>
</html>
