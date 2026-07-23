package com.bloom.model;

import javax.persistence.*;

/**
 * User entity — mapped to 'users' table via Hibernate
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "first_name", nullable = false, length = 60)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 60)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;   // stored as BCrypt hash

    @Column(name = "role", length = 10)
    private String role = "USER";  // USER or ADMIN

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new java.util.Date();
    }

    // ── Getters & Setters ──────────────────────────────────
    public int getUserId()            { return userId; }
    public void setUserId(int id)     { this.userId = id; }

    public String getFirstName()                { return firstName; }
    public void setFirstName(String firstName)  { this.firstName = firstName; }

    public String getLastName()                 { return lastName; }
    public void setLastName(String lastName)    { this.lastName = lastName; }

    public String getEmail()                    { return email; }
    public void setEmail(String email)          { this.email = email; }

    public String getPassword()                 { return password; }
    public void setPassword(String password)    { this.password = password; }

    public String getRole()                     { return role; }
    public void setRole(String role)            { this.role = role; }

    public java.util.Date getCreatedAt()        { return createdAt; }

    public String getFullName() { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return "User{id=" + userId + ", email=" + email + ", role=" + role + "}";
    }
}
