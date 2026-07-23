<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.admin}"><c:redirect url="../login.jsp"/></c:if>
<%
  if (request.getAttribute("products") == null && session.getAttribute("admin") != null) {
    com.bloom.dao.ProductDAO productDAO = new com.bloom.dao.ProductDAO();
    request.setAttribute("products", productDAO.findAll());
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Manage Products · Bloom Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bloom.css"/>
</head>
<body>
<div class="admin-layout">
  <aside class="admin-sidebar">
    <a href="${pageContext.request.contextPath}/AdminServlet" class="admin-logo">Bl<span>oo</span>m</a>
    <div class="admin-nav-section">
      <div class="admin-nav-label">Overview</div>
      <a href="${pageContext.request.contextPath}/AdminServlet" class="admin-nav-item">📊 Dashboard</a>
      <a href="${pageContext.request.contextPath}/admin/manage-products.jsp" class="admin-nav-item active">🌿 Products</a>
      <a href="#"                     class="admin-nav-item">👥 Users</a>
      <a href="#"                     class="admin-nav-item">📦 Orders</a>
    </div>
    <div class="admin-nav-section">
      <div class="admin-nav-label">Settings</div>
      <a href="${pageContext.request.contextPath}/index.jsp" class="admin-nav-item">🏠 View Store</a>
      <a href="${pageContext.request.contextPath}/LogoutServlet" class="admin-nav-item" style="color:rgba(255,100,100,0.6);">🚪 Sign Out</a>
    </div>
  </aside>

  <main class="admin-main">
    <div class="admin-topbar">
      <h1 class="admin-page-title">Products</h1>
      <button class="btn btn-terra" onclick="toggleForm()">+ Add Product</button>
    </div>

    <c:if test="${not empty sessionScope.adminSuccess}">
      <div class="alert alert-success">${sessionScope.adminSuccess}</div>
      <% session.removeAttribute("adminSuccess"); %>
    </c:if>

    <!-- Add Product Form (toggle) -->
    <div id="addForm" style="display:${param.action == 'add' ? 'block' : 'none'};margin-bottom:24px;">
      <div class="checkout-section">
        <div class="checkout-section-title"><span class="step-num active">+</span> Add New Product</div>
        <form action="${pageContext.request.contextPath}/ProductServlet" method="post">
          <input type="hidden" name="action" value="add"/>
          <div class="field-row">
            <div class="field"><label>Product Name</label>
              <input type="text" name="name" placeholder="Monstera Deliciosa" required/></div>
            <div class="field"><label>Category</label>
              <select name="category" required>
                <option value="Plants">🌿 Plants</option>
                <option value="Pots">🪴 Pots</option>
                <option value="Care">💧 Care</option>
              </select>
            </div>
          </div>
          <div class="field"><label>Description</label>
            <textarea name="description" placeholder="Product description..."></textarea></div>
          <div class="field-row">
            <div class="field"><label>Price (₹)</label>
              <input type="number" name="price" placeholder="599" step="0.01" min="0" required/></div>
            <div class="field"><label>Stock (units)</label>
              <input type="number" name="stock" placeholder="50" min="0" required/></div>
          </div>
          <div class="field"><label>Image URL (optional)</label>
            <input type="url" name="imageUrl" placeholder="https://..."/></div>
          <div class="field" style="display:flex;align-items:center;gap:10px;">
            <input type="checkbox" name="featured" id="featured" style="width:auto;"/>
            <label for="featured" style="text-transform:none;letter-spacing:0;font-size:0.88rem;color:var(--charcoal);">
              Feature on homepage
            </label>
          </div>
          <div style="display:flex;gap:12px;">
            <button type="submit" class="btn btn-terra">Save Product</button>
            <button type="button" class="btn btn-outline" onclick="toggleForm()">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Products table -->
    <div class="admin-table-wrap">
      <div class="admin-table-header">
        <div class="admin-table-title">All Products (${products.size()})</div>
      </div>
      <table>
        <thead>
          <tr><th>#</th><th>Name</th><th>Category</th><th>Price</th><th>Stock</th><th>Featured</th><th>Actions</th></tr>
        </thead>
        <tbody>
          <c:forEach var="p" items="${products}">
            <tr>
              <td style="color:var(--terracotta);font-weight:600;">${p.productId}</td>
              <td style="font-weight:500;">${p.name}</td>
              <td>${p.category}</td>
              <td style="font-family:var(--font-serif);">₹${p.price}</td>
              <td>
                <span style="color:${p.stock < 5 ? 'var(--terracotta)' : 'var(--sage)'};font-weight:600;">
                  ${p.stock}
                </span>
              </td>
              <td>${p.featured ? '⭐' : '—'}</td>
              <td>
                <div style="display:flex;gap:8px;">
                  <a href="${pageContext.request.contextPath}/admin/manage-products.jsp?edit=${p.productId}" class="btn btn-outline btn-sm">Edit</a>
                  <form action="${pageContext.request.contextPath}/ProductServlet" method="post" style="display:inline;"
                        onsubmit="return confirm('Delete ${p.name}?')">
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="productId" value="${p.productId}"/>
                    <button type="submit" class="btn btn-sm"
                            style="background:transparent;color:var(--terracotta);border:1px solid rgba(196,113,74,0.3);">
                      Delete
                    </button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty products}">
            <tr><td colspan="7" style="text-align:center;color:var(--muted);padding:40px;">No products found.</td></tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </main>
</div>
<script>
function toggleForm() {
  const form = document.getElementById('addForm');
  form.style.display = form.style.display === 'none' ? 'block' : 'none';
}
</script>
</body>
</html>
