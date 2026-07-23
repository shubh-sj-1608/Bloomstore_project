<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:if test="${empty sessionScope.user}">
  <c:redirect url="login.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Checkout · Bloom</title>
  <link rel="stylesheet" href="css/bloom.css"/>
</head>
<body>

<nav class="nav">
  <div class="nav-inner">
    <a href="index.jsp" class="nav-logo">Bl<span>oo</span>m</a>
    <ul class="nav-links">
      <li><a href="cart.jsp">← Back to Cart</a></li>
    </ul>
    <div class="nav-actions">
      <span style="font-size:0.83rem;color:var(--muted)">
        Checking out as ${sessionScope.user.firstName}
      </span>
      <a href="cart.jsp" class="cart-icon">🛒
        <span class="cart-count">${sessionScope.cartCount}</span>
      </a>
    </div>
  </div>
</nav>

<div style="max-width:1200px;margin:0 auto;padding:40px 48px 20px;">
  <span class="section-tag">Almost There</span>
  <h1 class="page-title">Checkout</h1>
</div>

<c:if test="${not empty error}">
  <div style="max-width:1100px;margin:0 auto;padding:0 48px;">
    <div class="alert alert-error">⚠️ ${error}</div>
  </div>
</c:if>

<div class="checkout-layout">

  <!-- Left: Forms -->
  <div>
    <form action="CheckoutServlet" method="post" id="checkoutForm">

      <!-- Step 1: Delivery Address -->
      <div class="checkout-section">
        <div class="checkout-section-title">
          <span class="step-num active">1</span>
          Delivery Address
        </div>
        <div class="field-row">
          <div class="field">
            <label>First Name</label>
            <input type="text" name="firstName" value="${sessionScope.user.firstName}" required/>
          </div>
          <div class="field">
            <label>Last Name</label>
            <input type="text" name="lastName" value="${sessionScope.user.lastName}" required/>
          </div>
        </div>
        <div class="field">
          <label>Email</label>
          <input type="email" name="email" value="${sessionScope.user.email}" required/>
        </div>
        <div class="field">
          <label>Phone Number</label>
          <input type="tel" name="phone" placeholder="+91 98765 43210" required/>
        </div>
        <div class="field">
          <label>Street Address</label>
          <input type="text" name="address" placeholder="123, Garden Lane" required/>
        </div>
        <div class="field-row">
          <div class="field">
            <label>City</label>
            <input type="text" name="city" placeholder="Mumbai" required/>
          </div>
          <div class="field">
            <label>Pincode</label>
            <input type="text" name="pincode" placeholder="400001" required maxlength="6"/>
          </div>
        </div>
        <div class="field">
          <label>State</label>
          <select name="state" required>
            <option value="">Select State</option>
            <option>Maharashtra</option>
            <option>Delhi</option>
            <option>Karnataka</option>
            <option>Tamil Nadu</option>
            <option>Gujarat</option>
            <option>Rajasthan</option>
            <option>West Bengal</option>
          </select>
        </div>
      </div>

      <!-- Step 2: Payment (RMI) -->
      <div class="checkout-section">
        <div class="checkout-section-title">
          <span class="step-num active">2</span>
          Payment
          <span class="rmi-badge" style="margin-left:auto;">🔒 RMI Secured</span>
        </div>

        <div style="background:var(--parchment);border-radius:var(--radius);padding:14px 16px;margin-bottom:20px;font-size:0.83rem;color:var(--muted);">
          💡 Payment is processed via <strong>RMI (Remote Method Invocation)</strong> — a distributed Java service that validates and authorises transactions securely.
        </div>

        <div class="payment-methods">
          <label class="payment-option selected" id="opt-card">
            <input type="radio" name="paymentMethod" value="card" checked
                   onchange="showPaymentFields('card')"/>
            <div class="payment-icon">💳</div>
            <span class="payment-label">Credit/Debit Card</span>
          </label>
          <label class="payment-option" id="opt-upi">
            <input type="radio" name="paymentMethod" value="upi"
                   onchange="showPaymentFields('upi')"/>
            <div class="payment-icon">📱</div>
            <span class="payment-label">UPI</span>
          </label>
          <label class="payment-option" id="opt-netbanking">
            <input type="radio" name="paymentMethod" value="netbanking"
                   onchange="showPaymentFields('netbanking')"/>
            <div class="payment-icon">🏦</div>
            <span class="payment-label">Net Banking</span>
          </label>
          <label class="payment-option" id="opt-cod">
            <input type="radio" name="paymentMethod" value="cod"
                   onchange="showPaymentFields('cod')"/>
            <div class="payment-icon">📦</div>
            <span class="payment-label">Cash on Delivery</span>
          </label>
        </div>

        <!-- Card fields -->
        <div id="fields-card">
          <div class="field">
            <label>Card Number</label>
            <input type="text" name="cardNumber" placeholder="1234 5678 9012 3456" maxlength="19"
                   oninput="formatCard(this)"/>
          </div>
          <div class="field-row">
            <div class="field">
              <label>Expiry Date</label>
              <input type="text" name="expiry" placeholder="MM / YY" maxlength="7"/>
            </div>
            <div class="field">
              <label>CVV</label>
              <input type="password" name="cvv" placeholder="•••" maxlength="3"/>
            </div>
          </div>
          <div class="field">
            <label>Name on Card</label>
            <input type="text" name="cardName" placeholder="ARJUN SHARMA"/>
          </div>
        </div>

        <!-- UPI fields -->
        <div id="fields-upi" style="display:none;">
          <div class="field">
            <label>UPI ID</label>
            <input type="text" name="upiId" placeholder="yourname@upi"/>
          </div>
        </div>

        <!-- Net banking fields -->
        <div id="fields-netbanking" style="display:none;">
          <div class="field">
            <label>Select Bank</label>
            <select name="bank">
              <option value="">Choose your bank</option>
              <option>SBI</option><option>HDFC</option><option>ICICI</option>
              <option>Axis Bank</option><option>Kotak</option><option>Yes Bank</option>
            </select>
          </div>
        </div>

        <!-- COD info -->
        <div id="fields-cod" style="display:none;">
          <div class="alert alert-info">
            📦 Pay ₹<fmt:formatNumber value="${sessionScope.cartTotal + (sessionScope.cartTotal >= 999 ? 0 : 99) + sessionScope.cartTotal * 0.05}" maxFractionDigits="0"/>
            in cash when your order is delivered. An additional ₹25 COD fee applies.
          </div>
        </div>
      </div>

      <button type="submit" class="btn btn-terra btn-lg" style="width:100%;justify-content:center;">
        🔒 Place Order · ₹<fmt:formatNumber value="${sessionScope.cartTotal + (sessionScope.cartTotal >= 999 ? 0 : 99) + sessionScope.cartTotal * 0.05}" maxFractionDigits="0"/>
      </button>

    </form>
  </div>

  <!-- Right: Order summary -->
  <div>
    <div class="cart-summary">
      <div class="summary-title">Order Summary</div>
      <c:forEach var="item" items="${sessionScope.cart.items}">
        <div style="display:flex;justify-content:space-between;align-items:center;padding:10px 0;border-bottom:1px solid var(--border);font-size:0.87rem;">
          <div>
            <div style="color:var(--charcoal);font-weight:500;">${item.product.name}</div>
            <div style="color:var(--muted);font-size:0.78rem;">Qty: ${item.quantity} × ₹${item.product.price}</div>
          </div>
          <div style="font-family:var(--font-serif);font-size:1rem;font-weight:600;">₹${item.product.price * item.quantity}</div>
        </div>
      </c:forEach>
      <div class="summary-row"><span>Subtotal</span><span>₹${sessionScope.cartTotal}</span></div>
      <div class="summary-row"><span>Shipping</span><span>${sessionScope.cartTotal >= 999 ? 'Free' : '₹99'}</span></div>
      <div class="summary-row"><span>Tax (5%)</span>
        <span>₹<fmt:formatNumber value="${sessionScope.cartTotal * 0.05}" maxFractionDigits="0"/></span>
      </div>
      <div class="summary-total">
        <span>Total</span>
        <span>₹<fmt:formatNumber value="${sessionScope.cartTotal + (sessionScope.cartTotal >= 999 ? 0 : 99) + sessionScope.cartTotal * 0.05}" maxFractionDigits="0"/></span>
      </div>
    </div>
  </div>

</div>

<script>
function showPaymentFields(method) {
  ['card','upi','netbanking','cod'].forEach(m => {
    document.getElementById('fields-' + m).style.display = (m === method) ? 'block' : 'none';
    document.getElementById('opt-' + m).classList.toggle('selected', m === method);
  });
}

function formatCard(input) {
  let v = input.value.replace(/\D/g,'').substring(0,16);
  input.value = v.replace(/(.{4})/g,'$1 ').trim();
}
</script>

</body>
</html>
