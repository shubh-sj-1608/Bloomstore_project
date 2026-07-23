<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.admin}"><c:redirect url="../login.jsp"/></c:if>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Admin Dashboard · Bloom</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bloom.css"/>
</head>
<body>
<div class="admin-layout">

  <!-- Sidebar -->
  <aside class="admin-sidebar">
    <a href="${pageContext.request.contextPath}/AdminServlet" class="admin-logo">Bl<span>oo</span>m</a>

    <div class="admin-nav-section">
      <div class="admin-nav-label">Overview</div>
      <a href="${pageContext.request.contextPath}/AdminServlet" class="admin-nav-item active">📊 Dashboard</a>
      <a href="${pageContext.request.contextPath}/admin/manage-products.jsp" class="admin-nav-item">🌿 Products</a>
      <a href="#" class="admin-nav-item">👥 Users</a>
      <a href="#" class="admin-nav-item">📦 Orders</a>
    </div>

    <div class="admin-nav-section">
      <div class="admin-nav-label">Settings</div>
      <a href="${pageContext.request.contextPath}/index.jsp" class="admin-nav-item">🏠 View Store</a>
      <a href="${pageContext.request.contextPath}/LogoutServlet" class="admin-nav-item" style="color:rgba(255,100,100,0.6);">🚪 Sign Out</a>
    </div>
  </aside>

  <!-- Main -->
  <main class="admin-main">
    <div class="admin-topbar">
      <div>
        <h1 class="admin-page-title">Dashboard</h1>
        <p style="font-size:0.83rem;color:var(--muted);margin-top:4px;">Welcome back, Admin</p>
      </div>
      <a href="${pageContext.request.contextPath}/admin/manage-products.jsp?action=add" class="btn btn-terra">+ Add Product</a>
    </div>

    <!-- Stats grid -->
    <div class="stats-grid">
      <div class="stat-card">
        <span class="stat-icon">💰</span>
        <div class="stat-value">₹${totalRevenue}</div>
        <div class="stat-label">Total Revenue</div>
        <div class="stat-change">↑ 12% this month</div>
      </div>
      <div class="stat-card">
        <span class="stat-icon">📦</span>
        <div class="stat-value">${totalOrders}</div>
        <div class="stat-label">Total Orders</div>
        <div class="stat-change">↑ 8 new today</div>
      </div>
      <div class="stat-card">
        <span class="stat-icon">👥</span>
        <div class="stat-value">${totalUsers}</div>
        <div class="stat-label">Registered Users</div>
        <div class="stat-change">↑ 3 new today</div>
      </div>
      <div class="stat-card">
        <span class="stat-icon">🌿</span>
        <div class="stat-value">${totalProducts}</div>
        <div class="stat-label">Products</div>
        <div class="stat-change">${lowStockCount} low stock</div>
      </div>
    </div>

    <!-- Recent orders table -->
    <div class="admin-table-wrap">
      <div class="admin-table-header">
        <div class="admin-table-title">Recent Orders</div>
        <a href="#" class="btn btn-outline btn-sm">View All</a>
      </div>
      <table>
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Customer</th>
            <th>Items</th>
            <th>Total</th>
            <th>Payment</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="order" items="${recentOrders}">
            <tr>
              <td style="font-weight:600;color:var(--terracotta);">#${order.orderId}</td>
              <td>${order.user.firstName} ${order.user.lastName}</td>
              <td>${order.items.size()} items</td>
              <td style="font-family:var(--font-serif);font-size:1rem;font-weight:600;">₹${order.totalAmount}</td>
              <td>
                <span class="rmi-badge">${order.paymentMethod}</span>
              </td>
              <td>
                <span class="status-badge status-${order.status.toLowerCase()}">${order.status}</span>
              </td>
              <td>
                <form action="${pageContext.request.contextPath}/AdminServlet" method="post" style="display:inline;">
                  <input type="hidden" name="action" value="updateStatus"/>
                  <input type="hidden" name="orderId" value="${order.orderId}"/>
                  <select name="status" onchange="this.form.submit()"
                          style="font-size:0.78rem;padding:5px;border:1px solid var(--border);border-radius:4px;background:white;">
                    <option value="Pending"    ${order.status == 'Pending'    ? 'selected' : ''}>Pending</option>
                    <option value="Confirmed"  ${order.status == 'Confirmed'  ? 'selected' : ''}>Confirmed</option>
                    <option value="Shipped"    ${order.status == 'Shipped'    ? 'selected' : ''}>Shipped</option>
                    <option value="Delivered"  ${order.status == 'Delivered'  ? 'selected' : ''}>Delivered</option>
                    <option value="Cancelled"  ${order.status == 'Cancelled'  ? 'selected' : ''}>Cancelled</option>
                  </select>
                </form>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty recentOrders}">
            <tr><td colspan="7" style="text-align:center;color:var(--muted);padding:40px;">No orders yet.</td></tr>
          </c:if>
        </tbody>
      </table>
    </div>

    <!-- Low stock alert -->
    <c:if test="${not empty lowStockProducts}">
      <div style="margin-top:28px;" class="admin-table-wrap">
        <div class="admin-table-header">
          <div class="admin-table-title" style="color:var(--terracotta);">⚠️ Low Stock Alert</div>
          <a href="${pageContext.request.contextPath}/admin/manage-products.jsp" class="btn btn-outline btn-sm">Manage Inventory</a>
        </div>
        <table>
          <thead><tr><th>Product</th><th>Category</th><th>Stock Left</th><th>Price</th><th>Action</th></tr></thead>
          <tbody>
            <c:forEach var="p" items="${lowStockProducts}">
              <tr>
                <td style="font-weight:500;">${p.name}</td>
                <td>${p.category}</td>
                <td><span style="color:var(--terracotta);font-weight:700;">${p.stock}</span></td>
                <td>₹${p.price}</td>
                <td><a href="${pageContext.request.contextPath}/admin/manage-products.jsp?edit=${p.productId}" class="btn btn-outline btn-sm">Update Stock</a></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </c:if>

  </main>
</div>
</body>
</html>
