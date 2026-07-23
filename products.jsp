<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  if (request.getAttribute("products") == null) {
    com.bloom.dao.ProductDAO productDAO = new com.bloom.dao.ProductDAO();
    String search = request.getParameter("search");
    String category = request.getParameter("cat");
    java.util.List<com.bloom.model.Product> products;

    if (search != null && !search.trim().isEmpty()) {
      products = productDAO.search(search.trim());
    } else if ("new".equalsIgnoreCase(category)) {
      products = productDAO.findRecent(8);
    } else if (category != null && !category.trim().isEmpty()) {
      String cat = category.trim().toLowerCase();
      if ("plants".equals(cat)) {
        products = productDAO.findByCategory("Plants");
      } else if ("pots".equals(cat)) {
        products = productDAO.findByCategory("Pots");
      } else if ("care".equals(cat)) {
        products = productDAO.findByCategory("Care");
      } else {
        products = productDAO.findAll();
      }
    } else {
      products = productDAO.findAll();
    }
    request.setAttribute("products", products);
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Shop · Bloom</title>
  <link rel="stylesheet" href="css/bloom.css"/>
</head>
<body>

<nav class="nav">
  <div class="nav-inner">
    <a href="index.jsp" class="nav-logo">Bl<span>oo</span>m</a>
    <ul class="nav-links">
      <li><a href="products.jsp" class="active">Shop</a></li>
      <li><a href="products.jsp?cat=plants">Plants</a></li>
      <li><a href="products.jsp?cat=pots">Pots</a></li>
      <li><a href="products.jsp?cat=care">Plant Care</a></li>
    </ul>
    <div class="nav-actions">
      <c:choose>
        <c:when test="${not empty sessionScope.user}">
          <span style="font-size:0.83rem;color:var(--muted)">Hi, ${sessionScope.user.firstName}</span>
          <a href="LogoutServlet" class="btn btn-ghost btn-sm">Sign Out</a>
        </c:when>
        <c:otherwise>
          <a href="login.jsp" class="btn btn-outline btn-sm">Sign In</a>
        </c:otherwise>
      </c:choose>
      <a href="cart.jsp" class="cart-icon">🛒
        <span class="cart-count">${empty sessionScope.cartCount ? 0 : sessionScope.cartCount}</span>
      </a>
    </div>
  </div>
</nav>

<!-- Page header -->
<div style="background:var(--parchment);padding:48px 48px 36px;border-bottom:1px solid var(--border);">
  <div style="max-width:1200px;margin:0 auto;">
    <div style="display:flex;justify-content:space-between;align-items:flex-end;flex-wrap:wrap;gap:20px;">
      <div>
        <span class="section-tag">Our Collection</span>
        <h1 class="page-title" style="margin-bottom:0;">
          <c:choose>
            <c:when test="${param.cat == 'plants'}">Tropical Plants</c:when>
            <c:when test="${param.cat == 'pots'}">Pots & Planters</c:when>
            <c:when test="${param.cat == 'care'}">Plant Care</c:when>
            <c:otherwise>All Products</c:otherwise>
          </c:choose>
        </h1>
      </div>
      <!-- Search -->
      <form action="ProductServlet" method="get" class="search-bar">
        <input type="text" name="search" placeholder="Search plants..." value="${param.search}"/>
        <button type="submit" class="btn btn-primary btn-sm">Search</button>
      </form>
    </div>

    <!-- Category filters -->
    <div style="display:flex;gap:10px;margin-top:24px;flex-wrap:wrap;">
      <a href="products.jsp" class="btn btn-sm ${empty param.cat ? 'btn-primary' : 'btn-outline'}">All</a>
      <a href="products.jsp?cat=plants" class="btn btn-sm ${param.cat == 'plants' ? 'btn-primary' : 'btn-outline'}">🌿 Plants</a>
      <a href="products.jsp?cat=pots" class="btn btn-sm ${param.cat == 'pots' ? 'btn-primary' : 'btn-outline'}">🪴 Pots</a>
      <a href="products.jsp?cat=care" class="btn btn-sm ${param.cat == 'care' ? 'btn-primary' : 'btn-outline'}">💧 Plant Care</a>
      <a href="products.jsp?cat=new" class="btn btn-sm ${param.cat == 'new' ? 'btn-terra' : 'btn-outline'}">✨ New</a>
    </div>
  </div>
</div>

<div class="section">
  <c:if test="${not empty successMsg}">
    <div class="alert alert-success">✅ ${successMsg}</div>
  </c:if>
  <c:if test="${not empty errorMsg}">
    <div class="alert alert-error">⚠️ ${errorMsg}</div>
  </c:if>

  <c:choose>
    <c:when test="${not empty products}">
      <div style="font-size:0.82rem;color:var(--muted);margin-bottom:20px;">
        Showing ${products.size()} products
      </div>
      <div class="products-grid">
        <c:forEach var="p" items="${products}">
          <div class="product-card">
            <div class="product-img">
              <c:choose>
                <c:when test="${not empty p.imageUrl}"><img src="${p.imageUrl}" alt="${p.name}"/></c:when>
                <c:otherwise>
                  <c:choose>
                    <c:when test="${p.category == 'Plants'}">🌿</c:when>
                    <c:when test="${p.category == 'Pots'}">🪴</c:when>
                    <c:when test="${p.category == 'Care'}">💧</c:when>
                    <c:otherwise>🌱</c:otherwise>
                  </c:choose>
                </c:otherwise>
              </c:choose>
              <c:if test="${p.stock == 0}">
                <span class="product-badge" style="background:var(--muted)">Out of Stock</span>
              </c:if>
              <c:if test="${p.stock > 0 && p.stock < 5}">
                <span class="product-badge">Low Stock</span>
              </c:if>
            </div>
            <div class="product-info">
              <div class="product-category">${p.category}</div>
              <div class="product-name">${p.name}</div>
              <p style="font-size:0.82rem;color:var(--muted);margin-bottom:6px;line-height:1.5;">${p.description}</p>
              <div style="font-size:0.78rem;color:var(--muted);margin-bottom:10px;">
                Stock: ${p.stock} left
              </div>
              <div class="product-footer">
                <div class="product-price">₹${p.price}</div>
                <c:choose>
                  <c:when test="${p.stock > 0}">
                    <form action="CartServlet" method="post">
                      <input type="hidden" name="action" value="add"/>
                      <input type="hidden" name="productId" value="${p.productId}"/>
                      <button type="submit" class="add-to-cart-btn" title="Add to cart">+</button>
                    </form>
                  </c:when>
                  <c:otherwise>
                    <button class="add-to-cart-btn" disabled style="background:var(--muted);cursor:not-allowed;">×</button>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </c:when>
    <c:otherwise>
      <div class="empty-state">
        <div class="empty-icon">🌱</div>
        <div class="empty-title">No products found</div>
        <p>Try a different category or search term.</p>
        <a href="products.jsp" class="btn btn-outline" style="margin-top:20px;">View All</a>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<footer class="footer">
  <div class="footer-bottom" style="max-width:1200px;margin:0 auto;padding-top:0;border-top:none;">
    <span>© 2026 Bloom Store · Advanced Java Mini Project</span>
    <span>JSP · Servlets · JavaBeans · Hibernate · RMI</span>
  </div>
</footer>

</body>
</html>
