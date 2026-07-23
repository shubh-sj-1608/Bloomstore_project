package com.bloom.servlet;

import com.bloom.dao.*;
import com.bloom.model.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

/**
 * AdminServlet — handles admin dashboard data loading
 * and order status updates.
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {

    private final OrderDAO   orderDAO   = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final UserDAO    userDAO    = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (!isAdmin(req)) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Load dashboard stats
        req.setAttribute("totalRevenue",   (long) orderDAO.getTotalRevenue());
        req.setAttribute("totalOrders",    orderDAO.countAll());
        req.setAttribute("totalUsers",     userDAO.countAll());
        req.setAttribute("totalProducts",  productDAO.countAll());
        req.setAttribute("recentOrders",   orderDAO.findRecent(10));
        req.setAttribute("lowStockProducts", productDAO.findLowStock());
        req.setAttribute("lowStockCount",  productDAO.findLowStock().size());

        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (!isAdmin(req)) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");

        if ("updateStatus".equals(action)) {
            int    orderId = Integer.parseInt(req.getParameter("orderId"));
            String status  = req.getParameter("status");
            orderDAO.updateStatus(orderId, status);
            req.getSession().setAttribute("adminSuccess",
                "Order #" + orderId + " status updated to " + status);
        }

        res.sendRedirect(req.getContextPath() + "/admin/manage-orders.jsp");
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return false;
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole() != null ? user.getRole().trim() : "");
    }
}
