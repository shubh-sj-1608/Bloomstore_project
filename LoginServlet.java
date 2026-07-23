package com.bloom.servlet;

import com.bloom.bean.UserBean;
import com.bloom.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * LoginServlet — handles POST from login.jsp
 * Uses UserBean for authentication logic.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        UserBean userBean = new UserBean();
        User user = userBean.login(email, password);

        if (user != null) {
            // ── Login successful ──────────────────────────
            HttpSession oldSession = req.getSession(false);
            String redirect = oldSession == null ? null : (String) oldSession.getAttribute("redirectAfterLogin");
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("cartCount", 0);

            // Admin redirect
            if ("ADMIN".equalsIgnoreCase(user.getRole() != null ? user.getRole().trim() : "")) {
                session.setAttribute("admin", user);  // Set admin attribute for admin pages
                res.sendRedirect(req.getContextPath() + "/AdminServlet");
            } else {
                // Redirect back to intended page or home
                if (redirect != null) {
                    res.sendRedirect(redirect);
                } else {
                    res.sendRedirect(req.getContextPath() + "/index.jsp");
                }
            }
        } else {
            // ── Login failed ──────────────────────────────
            req.setAttribute("error", userBean.getErrorMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, res);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}
