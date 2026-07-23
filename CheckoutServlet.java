package com.bloom.servlet;

import com.bloom.bean.CartBean;
import com.bloom.dao.OrderDAO;
import com.bloom.dao.ProductDAO;
import com.bloom.model.*;
import com.bloom.rmi.PaymentResult;
import com.bloom.rmi.PaymentService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

/**
 * CheckoutServlet — processes order placement.
 *
 * Flow:
 *  1. Read delivery + payment details from form
 *  2. Call RMI PaymentService.processPayment()
 *  3. If approved → save Order via Hibernate
 *  4. Decrease stock for each product
 *  5. Clear cart from session
 *  6. Redirect to order-success.jsp
 */
@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    private static final String RMI_URL = "rmi://localhost/BloomPaymentService";

    private final OrderDAO   orderDAO   = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Guard: must be logged in
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Guard: cart must not be empty
        CartBean cart = (CartBean) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/cart.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        // ── 1. Read form data ─────────────────────────────
        String firstName     = req.getParameter("firstName");
        String lastName      = req.getParameter("lastName");
        String address       = req.getParameter("address");
        String city          = req.getParameter("city");
        String state         = req.getParameter("state");
        String pincode       = req.getParameter("pincode");
        String phone         = req.getParameter("phone");
        String paymentMethod = req.getParameter("paymentMethod");

        // Payment token (card/upi details)
        String paymentToken;
        if ("card".equals(paymentMethod)) {
            paymentToken = req.getParameter("cardNumber");
        } else if ("upi".equals(paymentMethod)) {
            paymentToken = req.getParameter("upiId");
        } else {
            paymentToken = paymentMethod; // cod/netbanking
        }

        String deliveryAddress = address + ", " + city + ", " + state + " - " + pincode;
        double totalAmount = cart.getTotal();

        // ── 2. Call RMI Payment Service ───────────────────
        PaymentResult paymentResult;
        try {
            PaymentService paymentService =
                (PaymentService) Naming.lookup(RMI_URL);
            paymentResult = paymentService.processPayment(
                0, totalAmount, paymentMethod, paymentToken);

        } catch (Exception e) {
            // RMI server unavailable — fallback for demo/testing
            System.err.println("⚠️ RMI lookup failed (server may not be running): " + e.getMessage());
            // For demo: simulate approval
            paymentResult = new PaymentResult(true,
                "DEMO-" + System.currentTimeMillis(), "Demo approval", "APPROVED");
        }

        if (!paymentResult.isSuccess()) {
            req.setAttribute("error", "Payment failed: " + paymentResult.getMessage());
            req.getRequestDispatcher("/checkout.jsp").forward(req, res);
            return;
        }

        // ── 3. Build and Save Order ───────────────────────
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus("Confirmed");
        order.setPaymentMethod(paymentMethod.toUpperCase());
        order.setPaymentStatus("PAID");
        order.setDeliveryAddress(deliveryAddress);

        // Convert CartBean items → Order items
        List<CartItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            CartItem item = new CartItem(cartItem.getProduct(), cartItem.getQuantity());
            item.setOrder(order);
            orderItems.add(item);
        }
        order.setItems(orderItems);

        Order savedOrder = orderDAO.save(order);

        if (savedOrder == null) {
            req.setAttribute("error", "Order could not be saved. Please contact support.");
            req.getRequestDispatcher("/checkout.jsp").forward(req, res);
            return;
        }

        // ── 4. Decrease product stock ─────────────────────
        for (CartItem item : cart.getItems()) {
            productDAO.decreaseStock(
                item.getProduct().getProductId(), item.getQuantity());
        }

        // ── 5. Clear cart ─────────────────────────────────
        cart.clear();
        session.setAttribute("cartCount", 0);
        session.setAttribute("cartTotal", 0);

        // ── 6. Redirect to success page ───────────────────
        session.setAttribute("lastOrder", savedOrder);
        session.setAttribute("lastTransactionId", paymentResult.getTransactionId());
        res.sendRedirect(req.getContextPath() + "/order-success.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + "/checkout.jsp");
    }
}
