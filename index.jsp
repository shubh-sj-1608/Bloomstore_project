<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  // Redirect to AdminServlet which will load data and forward to dashboard
  response.sendRedirect(request.getContextPath() + "/AdminServlet");
%>
