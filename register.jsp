<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty sessionScope.user}">
  <c:redirect url="index.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Create Account · Bloom</title>
  <link rel="stylesheet" href="css/bloom.css"/>
</head>
<body>
<div class="form-page">
  <div class="form-card" style="max-width:520px;">

    <div class="form-header">
      <a href="index.jsp" class="form-logo">Bl<span>oo</span>m</a>
      <h1 class="form-title">Join Bloom</h1>
      <p class="form-sub">Create your account and start shopping</p>
    </div>

    <div class="form-body">
      <c:if test="${not empty error}">
        <div class="alert alert-error">⚠️ ${error}</div>
      </c:if>

      <form action="RegisterServlet" method="post">
        <div class="field-row">
          <div class="field">
            <label>First Name</label>
            <input type="text" name="firstName" placeholder="Arjun" required/>
          </div>
          <div class="field">
            <label>Last Name</label>
            <input type="text" name="lastName" placeholder="Sharma" required/>
          </div>
        </div>
        <div class="field">
          <label>Email Address</label>
          <input type="email" name="email" placeholder="you@email.com" required/>
        </div>
        <div class="field">
          <label>Password</label>
          <input type="password" name="password" placeholder="Minimum 6 characters" required minlength="6"/>
        </div>
        <div class="field">
          <label>Confirm Password</label>
          <input type="password" name="confirmPassword" placeholder="Repeat your password" required/>
        </div>
        <button type="submit" class="btn btn-terra btn-lg" style="width:100%;justify-content:center;margin-top:8px;">
          Create Account →
        </button>
      </form>

      <div class="form-divider">or</div>

      <div class="form-footer-link">
        Already have an account? <a href="login.jsp">Sign in</a>
      </div>
    </div>
  </div>
</div>
</body>
</html>
