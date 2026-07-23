<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.user}"><c:redirect url="login.jsp"/></c:if>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>My Orders · Bloom</title>
  <link rel="stylesheet" href="css/bloom.css"/>
</head>
<body>

<nav class="nav">
  <div class="nav-inner">
    <a href="index.jsp" class="nav-logo">Bl<span>oo</span>m</a>
    <ul class="nav-links">
      <li><a href="products.jsp">Shop</a></li>
      <li><a href="orders.jsp" class="active">My Orders</a></li>
    </ul>
    <div class="nav-actions">
      <span style="font-size:0.83rem;color:var(--muted)">Hi, ${sessionScope.user.firstName}</span>
      <a href="LogoutServlet" class="btn btn-ghost btn-sm">Sign Out</a>
      <a href="cart.jsp" class="cart-icon">🛒<span class="cart-count">${sessionScope.cartCount}</span></a>
    </div>
  </div>
</nav>

<div class="orders-page">
  <span class="section-tag">Account</span>
  <h1 class="page-title">My Orders</h1>
  <p class="page-sub">All your plant orders in one place.</p>

  <c:if test="${not empty successMsg}">
    <div class="alert alert-success">✅ ${successMsg}</div>
  </c:if>

  <c:choose>
    <c:when test="${not empty orders}">
      <c:forEach var="order" items="${orders}">
        <div class="order-card">
          <div class="order-header">
            <div>
              <div class="order-id">Order #${order.orderId}</div>
              <div class="order-date">${order.formattedDate}</div>
            </div>
            <span class="status-badge status-${order.status.toLowerCase()}">${order.status}</span>
          </div>
          <div class="order-body">
            <div class="order-items-preview">
              <c:forEach var="item" items="${order.items}" begin="0" end="3">
                <div class="order-item-thumb">
                  <c:choose>
                    <c:when test="${item.product.category == 'Plants'}">🌿</c:when>
                    <c:when test="${item.product.category == 'Pots'}">🪴</c:when>
                    <c:otherwise>💧</c:otherwise>
                  </c:choose>
                </div>
              </c:forEach>
              <c:if test="${order.items.size() > 4}">
                <div class="order-item-thumb" style="font-size:0.75rem;font-weight:600;color:var(--muted);">
                  +${order.items.size() - 4}
                </div>
              </c:if>
            </div>
            <div style="font-size:0.85rem;color:var(--muted);">
              ${order.items.size()} item(s) · ${order.deliveryAddress}
            </div>
            <div class="order-footer">
              <div style="font-size:0.82rem;color:var(--muted);">
                Paid via ${order.paymentMethod}
                <span class="rmi-badge" style="margin-left:8px;">RMI</span>
              </div>
              <div class="order-total">₹${order.totalAmount}</div>
            </div>
          </div>
        </div>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="empty-state">
        <div class="empty-icon">📦</div>
        <div class="empty-title">No orders yet</div>
        <p>Your first order is just a few plants away.</p>
        <a href="products.jsp" class="btn btn-primary" style="margin-top:24px;">Start Shopping →</a>
      </div>
    </c:otherwise>
  </c:choose>
</div>

</body>
</html>
