package com.bloom.servlet;

import com.bloom.bean.UserBean;
import com.bloom.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * RegisterServlet — handles POST from register.jsp
 * Uses UserBean for registration and validation logic.
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String firstName       = req.getParameter("firstName");
        String lastName        = req.getParameter("lastName");
        String email           = req.getParameter("email");
        String password        = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        UserBean userBean = new UserBean();
        User user = userBean.register(firstName, lastName, email,
                                       password, confirmPassword);

        if (user != null) {
            // ── Success: redirect to login with message ──
            req.getSession().setAttribute("registrationSuccess",
                "Account created! Welcome to Bloom 🌿 Please sign in.");
            res.sendRedirect(req.getContextPath() + "/login.jsp");
        } else {
            // ── Validation failed: show errors in form ───
            req.setAttribute("error", userBean.getErrorMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, res);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + "/register.jsp");
    }
}
