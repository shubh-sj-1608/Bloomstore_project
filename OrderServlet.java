package com.bloom.servlet;

import com.bloom.dao.OrderDAO;
import com.bloom.model.Order;
import com.bloom.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

/**
 * OrderServlet — loads user's orders for orders.jsp
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            session.setAttribute("redirectAfterLogin",
                req.getContextPath() + "/orders.jsp");
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        List<Order> orders = orderDAO.findByUser(user.getUserId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/orders.jsp").forward(req, res);
    }
}
