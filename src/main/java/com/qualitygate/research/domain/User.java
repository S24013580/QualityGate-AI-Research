package com.qualitygate.research.domain;

import java.util.Objects;

/**
 * Domain entity representing a User
 * Part of the Domain Layer - represents business entities
 * Simplified for Service Layer testing
 */
public class User {
    
    private Long userId;
    private String username;
    private String email;
    private boolean active;
    
    public User() {
        this.active = true;
    }
    
    public User(Long userId, String username, String email) {
        this();
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
}

