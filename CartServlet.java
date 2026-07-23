package com.bloom.servlet;

import com.bloom.bean.CartBean;
import com.bloom.dao.ProductDAO;
import com.bloom.model.Product;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * CartServlet — manages cart via CartBean stored in session.
 *
 * Actions: add | remove | increase | decrease | clear
 * All POSTs redirect back to cart.jsp (PRG pattern).
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session  = req.getSession();
        String action        = req.getParameter("action");

        if (session.getAttribute("user") == null) {
            String referer = req.getHeader("Referer");
            if (referer != null && !referer.contains("LoginServlet")) {
                session.setAttribute("redirectAfterLogin", referer);
            }
            session.setAttribute("loginMessage", "Please sign in before adding products to your cart.");
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Get or create CartBean in session
        CartBean cart = (CartBean) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }

        switch (action != null ? action : "") {

            case "add": {
                String pidParam = req.getParameter("productId");
                if (pidParam != null) {
                    int productId = Integer.parseInt(pidParam);
                    Product product = productDAO.findById(productId);
                    if (product != null && product.getStock() > 0) {
                        cart.addProduct(product);
                        session.setAttribute("cartSuccess", "Added to cart!");
                    }
                }
                break;
            }

            case "remove": {
                int productId = Integer.parseInt(req.getParameter("productId"));
                cart.removeProduct(productId);
                break;
            }

            case "increase": {
                int productId = Integer.parseInt(req.getParameter("productId"));
                cart.increaseQuantity(productId);
                break;
            }

            case "decrease": {
                int productId = Integer.parseInt(req.getParameter("productId"));
                cart.decreaseQuantity(productId);
                break;
            }

            case "clear": {
                cart.clear();
                break;
            }
        }

        // Sync cart count and total to session for navbar badge
        session.setAttribute("cartCount", cart.getTotalCount());
        session.setAttribute("cartTotal", (int) cart.getSubtotal());

        // PRG redirect (prevents form re-submission on refresh)
        String referer = req.getHeader("Referer");
        if (referer != null && !referer.contains("CartServlet")
                && !referer.contains("checkout")) {
            res.sendRedirect(referer);
        } else {
            res.sendRedirect(req.getContextPath() + "/cart.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + "/cart.jsp");
    }
}
