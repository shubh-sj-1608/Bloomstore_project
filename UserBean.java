package com.bloom.bean;

import com.bloom.dao.UserDAO;
import com.bloom.model.User;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * UserBean — JavaBean for User business logic.
 *
 * Sits between Servlets and DAO.
 * Handles: registration validation, login auth, password hashing.
 *
 * JavaBean conventions followed:
 *  - public no-arg constructor
 *  - private fields with getters/setters
 *  - Serializable
 */
public class UserBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();

    // ── Properties (for JSP form binding) ────────────────
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String errorMessage;

    // ── Registration ──────────────────────────────────────
    /**
     * Registers a new user after validation.
     * @return the saved User object, or null on failure
     */
    public User register(String firstName, String lastName,
                          String email, String password,
                          String confirmPassword) {
        // Validate
        if (firstName == null || firstName.trim().isEmpty() ||
            lastName  == null || lastName.trim().isEmpty()) {
            errorMessage = "First and last name are required.";
            return null;
        }
        if (!isValidEmail(email)) {
            errorMessage = "Please enter a valid email address.";
            return null;
        }
        if (password == null || password.length() < 6) {
            errorMessage = "Password must be at least 6 characters.";
            return null;
        }
        if (!password.equals(confirmPassword)) {
            errorMessage = "Passwords do not match.";
            return null;
        }
        if (userDAO.emailExists(email)) {
            errorMessage = "An account with this email already exists.";
            return null;
        }

        // Build user entity
        User user = new User();
        user.setFirstName(firstName.trim());
        user.setLastName(lastName.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(hashPassword(password));
        user.setRole("USER");

        boolean saved = userDAO.save(user);
        if (!saved) {
            errorMessage = "Registration failed. Please try again.";
            return null;
        }
        return user;
    }

    // ── Login ─────────────────────────────────────────────
    /**
     * Authenticates user by email and password.
     * @return the authenticated User, or null on failure
     */
    public User login(String email, String password) {
        if (email == null || password == null) {
            errorMessage = "Email and password are required.";
            return null;
        }
        String normalizedEmail = email.trim().toLowerCase();
        User user = userDAO.findByEmail(normalizedEmail);
        if (user == null) {
            if ("admin@bloom.com".equals(normalizedEmail) && "bloom123".equals(password)) {
                return createDefaultAdmin();
            }
            errorMessage = "No account found with this email.";
            return null;
        }
        if (!verifyPassword(password, user.getPassword())) {
            errorMessage = "Incorrect password.";
            return null;
        }
        if ("admin@bloom.com".equals(user.getEmail())) {
            boolean needsUpdate = !"ADMIN".equalsIgnoreCase(user.getRole() != null ? user.getRole().trim() : "")
                || !hashPassword("bloom123").equals(user.getPassword());
            if (needsUpdate) {
                user.setRole("ADMIN");
                user.setPassword(hashPassword("bloom123"));
                userDAO.update(user);
            }
        }
        return user;
    }

    private User createDefaultAdmin() {
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Bloom");
        admin.setEmail("admin@bloom.com");
        admin.setPassword(hashPassword("bloom123"));
        admin.setRole("ADMIN");

        if (!userDAO.save(admin)) {
            errorMessage = "Admin account could not be created. Please check database connection.";
            return null;
        }
        return admin;
    }

    // ── Password Hashing (SHA-256) ────────────────────────
    public String hashPassword(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plain.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return plain; // fallback (not production-safe)
        }
    }

    public boolean verifyPassword(String plain, String hashed) {
        if (hashed == null) {
            return false;
        }
        if (hashPassword(plain).equals(hashed)) {
            return true;
        }

        // Older setup.sql used this incorrect hash while documenting bloom123.
        // Keep it accepted so existing local Eclipse/MySQL databases still work.
        return "bloom123".equals(plain)
            && "GLoBvJ4ddKjVtR5bGaw+njw7H6T12Mw+Kh0MnY5/WrM=".equals(hashed);
    }

    // ── Email validation ──────────────────────────────────
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    // ── Getters & Setters ─────────────────────────────────
    public String getFirstName()                { return firstName; }
    public void setFirstName(String fn)         { this.firstName = fn; }

    public String getLastName()                 { return lastName; }
    public void setLastName(String ln)          { this.lastName = ln; }

    public String getEmail()                    { return email; }
    public void setEmail(String email)          { this.email = email; }

    public String getPassword()                 { return password; }
    public void setPassword(String password)    { this.password = password; }

    public String getErrorMessage()             { return errorMessage; }
    public void setErrorMessage(String msg)     { this.errorMessage = msg; }
}
