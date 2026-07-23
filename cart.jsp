<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Your Cart · Bloom</title>
  <link rel="stylesheet" href="css/bloom.css"/>
</head>
<body>

<nav class="nav">
  <div class="nav-inner">
    <a href="index.jsp" class="nav-logo">Bl<span>oo</span>m</a>
    <ul class="nav-links">
      <li><a href="products.jsp">Shop</a></li>
      <li><a href="orders.jsp">Orders</a></li>
    </ul>
    <div class="nav-actions">
      <c:if test="${not empty sessionScope.user}">
        <span style="font-size:0.83rem;color:var(--muted)">Hi, ${sessionScope.user.firstName}</span>
        <a href="LogoutServlet" class="btn btn-ghost btn-sm">Sign Out</a>
      </c:if>
      <a href="cart.jsp" class="cart-icon">🛒
        <span class="cart-count">${empty sessionScope.cartCount ? 0 : sessionScope.cartCount}</span>
      </a>
    </div>
  </div>
</nav>

<div style="max-width:1200px;margin:0 auto;padding:40px 48px 20px;">
  <span class="section-tag">Shopping Bag</span>
  <h1 class="page-title">Your Cart</h1>
</div>

<c:choose>
  <c:when test="${not empty sessionScope.cart && sessionScope.cart.totalCount > 0}">
    <div class="cart-layout">

      <!-- Cart items -->
      <div>
        <c:if test="${not empty successMsg}">
          <div class="alert alert-success">✅ ${successMsg}</div>
        </c:if>

        <div class="cart-items">
          <c:forEach var="item" items="${sessionScope.cart.items}">
            <div class="cart-item">
              <div class="cart-item-img">
                <c:choose>
                  <c:when test="${not empty item.product.imageUrl}">
                    <img class="cart-thumb-img" src="${item.product.imageUrl}" alt="${item.product.name}"
                         width="80" height="80"
                         style="width:80px;height:80px;max-width:80px;max-height:80px;object-fit:cover;display:block;"/>
                  </c:when>
                  <c:when test="${item.product.category == 'Plants'}">🌿</c:when>
                  <c:when test="${item.product.category == 'Pots'}">🪴</c:when>
                  <c:otherwise>💧</c:otherwise>
                </c:choose>
              </div>
              <div class="cart-item-info">
                <div class="cart-item-name">${item.product.name}</div>
                <div class="cart-item-price">${item.product.category} · ₹${item.product.price} each</div>
              </div>

              <!-- Quantity controls -->
              <div class="qty-control">
                <form action="CartServlet" method="post" style="display:inline">
                  <input type="hidden" name="action" value="decrease"/>
                  <input type="hidden" name="productId" value="${item.product.productId}"/>
                  <button type="submit" class="qty-btn">−</button>
                </form>
                <span class="qty-num">${item.quantity}</span>
                <form action="CartServlet" method="post" style="display:inline">
                  <input type="hidden" name="action" value="increase"/>
                  <input type="hidden" name="productId" value="${item.product.productId}"/>
                  <button type="submit" class="qty-btn">+</button>
                </form>
              </div>

              <div class="cart-item-total">₹${item.product.price * item.quantity}</div>

              <!-- Remove -->
              <form action="CartServlet" method="post">
                <input type="hidden" name="action" value="remove"/>
                <input type="hidden" name="productId" value="${item.product.productId}"/>
                <button type="submit" class="btn btn-ghost btn-sm" style="color:var(--muted);">✕</button>
              </form>
            </div>
          </c:forEach>
        </div>

        <div style="margin-top:20px;display:flex;gap:12px;">
          <a href="products.jsp" class="btn btn-outline">← Continue Shopping</a>
          <form action="CartServlet" method="post">
            <input type="hidden" name="action" value="clear"/>
            <button type="submit" class="btn btn-ghost" style="color:var(--muted);">Clear Cart</button>
          </form>
        </div>
      </div>

      <!-- Summary -->
      <div>
        <div class="cart-summary">
          <div class="summary-title">Order Summary</div>

          <div class="summary-row">
            <span>Subtotal (${sessionScope.cartCount} items)</span>
            <span>₹${sessionScope.cartTotal}</span>
          </div>
          <div class="summary-row">
            <span>Shipping</span>
            <span>${sessionScope.cartTotal >= 999 ? 'Free 🎉' : '₹99'}</span>
          </div>
          <div class="summary-row">
            <span>Tax (5%)</span>
            <span>₹<fmt:formatNumber value="${sessionScope.cartTotal * 0.05}" maxFractionDigits="0"/></span>
          </div>

          <div class="summary-total">
            <span>Total</span>
            <span>₹<fmt:formatNumber value="${sessionScope.cartTotal + (sessionScope.cartTotal >= 999 ? 0 : 99) + sessionScope.cartTotal * 0.05}" maxFractionDigits="0"/></span>
          </div>

          <c:choose>
            <c:when test="${not empty sessionScope.user}">
              <a href="checkout.jsp" class="btn btn-terra btn-lg" style="width:100%;justify-content:center;">
                Proceed to Checkout →
              </a>
            </c:when>
            <c:otherwise>
              <a href="login.jsp" class="btn btn-terra btn-lg" style="width:100%;justify-content:center;">
                Sign in to Checkout →
              </a>
            </c:otherwise>
          </c:choose>

          <div style="margin-top:16px;text-align:center;">
            <span class="rmi-badge">🔒 Secured by RMI Payment Gateway</span>
          </div>
        </div>
      </div>

    </div>
  </c:when>
  <c:otherwise>
    <div style="padding:0 48px 80px;">
      <div class="empty-state">
        <div class="empty-icon">🛒</div>
        <div class="empty-title">Your cart is empty</div>
        <p>Looks like you haven't added any plants yet.</p>
        <a href="products.jsp" class="btn btn-primary" style="margin-top:24px;">Browse Plants →</a>
      </div>
    </div>
  </c:otherwise>
</c:choose>

</body>
</html>
