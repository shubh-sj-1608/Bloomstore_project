<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Sign In · Bloom</title>
  <link rel="stylesheet" href="css/bloom.css"/>
</head>
<body>
<div class="form-page">
  <div class="form-card">

    <div class="form-header">
      <a href="index.jsp" class="form-logo">Bl<span>oo</span>m</a>
      <h1 class="form-title">Welcome back</h1>
      <p class="form-sub">Sign in to your Bloom account</p>
    </div>

    <div class="form-body">

      <c:if test="${not empty error}">
        <div class="alert alert-error">⚠️ ${error}</div>
      </c:if>
      <c:if test="${not empty success}">
        <div class="alert alert-success">✅ ${success}</div>
      </c:if>

      <c:if test="${not empty sessionScope.loginMessage}">
        <div class="alert alert-error">${sessionScope.loginMessage}</div>
        <% session.removeAttribute("loginMessage"); %>
      </c:if>
      <c:if test="${not empty sessionScope.user}">
        <div class="alert alert-success">
          Signed in as ${sessionScope.user.email}. Enter another account below or
          <a href="LogoutServlet">sign out</a>.
        </div>
      </c:if>

      <form action="LoginServlet" method="post">
        <div class="field">
          <label>Email Address</label>
          <input type="email" name="email" placeholder="you@email.com" required
                 value="${param.email}"/>
        </div>
        <div class="field">
          <label>Password</label>
          <input type="password" name="password" placeholder="Enter your password" required/>
        </div>
        <button type="submit" class="btn btn-primary btn-lg" style="width:100%;justify-content:center;margin-top:8px;">
          Sign In →
        </button>
      </form>

      <div class="form-divider">or</div>

      <div class="form-footer-link">
        New to Bloom? <a href="register.jsp">Create an account</a>
      </div>
    </div>
  </div>
</div>
</body>
</html>
