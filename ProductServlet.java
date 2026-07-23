package com.bloom.servlet;

import com.bloom.dao.ProductDAO;
import com.bloom.model.Product;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

/**
 * ProductServlet — loads products for products.jsp and index.jsp
 * Also handles admin product management (add/edit/delete).
 */
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action   = req.getParameter("action");
        String category = req.getParameter("cat");
        String search   = req.getParameter("search");

        List<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productDAO.search(search.trim());
        } else if ("new".equalsIgnoreCase(category)) {
            products = productDAO.findRecent(8);
        } else if (category != null && !category.isEmpty()) {
            // Map URL param to DB category name
            String cat = mapCategory(category);
            products = productDAO.findByCategory(cat);
        } else {
            products = productDAO.findAll();
        }

        req.setAttribute("products", products);
        req.getRequestDispatcher("/products.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || !isAdmin(session)) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");

        if ("add".equals(action) || "edit".equals(action)) {
            Product product;
            if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("productId"));
                product = productDAO.findById(id);
                if (product == null) product = new Product();
            } else {
                product = new Product();
            }

            product.setName(req.getParameter("name").trim());
            product.setDescription(req.getParameter("description").trim());
            product.setPrice(Double.parseDouble(req.getParameter("price")));
            product.setStock(Integer.parseInt(req.getParameter("stock")));
            product.setCategory(req.getParameter("category"));
            product.setImageUrl(req.getParameter("imageUrl"));
            product.setFeatured("on".equals(req.getParameter("featured")));

            if ("edit".equals(action)) productDAO.update(product);
            else                        productDAO.save(product);

            req.getSession().setAttribute("adminSuccess",
                "Product " + ("add".equals(action) ? "added" : "updated") + " successfully.");

        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("productId"));
            productDAO.delete(id);
            req.getSession().setAttribute("adminSuccess", "Product deleted.");

        } else if ("updateStock".equals(action)) {
            int id    = Integer.parseInt(req.getParameter("productId"));
            int stock = Integer.parseInt(req.getParameter("stock"));
            Product p = productDAO.findById(id);
            if (p != null) {
                p.setStock(stock);
                productDAO.update(p);
            }
        }

        res.sendRedirect(req.getContextPath() + "/admin/manage-products.jsp");
    }

    private String mapCategory(String urlParam) {
        switch (urlParam.toLowerCase()) {
            case "plants":      return "Plants";
            case "pots":        return "Pots";
            case "care":        return "Care";
            default:            return urlParam;
        }
    }

    private boolean isAdmin(HttpSession session) {
        Object admin = session.getAttribute("admin");
        if (admin instanceof com.bloom.model.User) {
            com.bloom.model.User user = (com.bloom.model.User) admin;
            return "ADMIN".equalsIgnoreCase(user.getRole() != null ? user.getRole().trim() : "");
        }

        Object currentUser = session.getAttribute("user");
        if (currentUser instanceof com.bloom.model.User) {
            com.bloom.model.User user = (com.bloom.model.User) currentUser;
            if ("ADMIN".equalsIgnoreCase(user.getRole() != null ? user.getRole().trim() : "")) {
                session.setAttribute("admin", user);
                return true;
            }
        }
        return false;
    }
}
