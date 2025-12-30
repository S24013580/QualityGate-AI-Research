package com.qualitygate.research.service;

import com.qualitygate.research.domain.User;

/**
 * Service Layer - UserService
 * 
 * Designed to be as simple as possible. It does validation, returning determinate answers
 * with very little branching.
 * 
 * The contrast between OrderService and UserService enables the study to distinguish
 * the effect of the logic complexity level on the test quality produced by AI.
 */
public class UserService {
    
    /**
     * Validates a user's email address.
     * Simple validation with minimal branching.
     * 
     * @param email The email address to validate
     * @return true if email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return email.contains("@") && email.contains(".");
    }
    
    /**
     * Validates a user's username.
     * Simple validation with minimal branching.
     * 
     * @param username The username to validate
     * @return true if username is valid, false otherwise
     */
    public boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        int length = username.trim().length();
        return length >= 3 && length <= 50;
    }
    
    /**
     * Creates a new user with validation.
     * Simple method with explicit parameter validation.
     * 
     * @param username The username
     * @param email The email address
     * @return A new User object if valid, null otherwise
     */
    public User createUser(String username, String email) {
        if (!isValidUsername(username)) {
            return null;
        }
        
        if (!isValidEmail(email)) {
            return null;
        }
        
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        
        return user;
    }
    
    /**
     * Updates a user's email address.
     * Simple method with validation.
     * 
     * @param user The user to update
     * @param newEmail The new email address
     * @return true if update was successful, false otherwise
     */
    public boolean updateUserEmail(User user, String newEmail) {
        if (user == null) {
            return false;
        }
        
        if (!isValidEmail(newEmail)) {
            return false;
        }
        
        user.setEmail(newEmail.trim());
        return true;
    }
    
    /**
     * Activates a user account.
     * Simple state change with minimal logic.
     * 
     * @param user The user to activate
     * @return true if activation was successful, false otherwise
     */
    public boolean activateUser(User user) {
        if (user == null) {
            return false;
        }
        
        user.setActive(true);
        return true;
    }
    
    /**
     * Deactivates a user account.
     * Simple state change with minimal logic.
     * 
     * @param user The user to deactivate
     * @return true if deactivation was successful, false otherwise
     */
    public boolean deactivateUser(User user) {
        if (user == null) {
            return false;
        }
        
        user.setActive(false);
        return true;
    }
    
    /**
     * Checks if a user is active.
     * Simple getter-like method.
     * 
     * @param user The user to check
     * @return true if user is active, false otherwise
     */
    public boolean isUserActive(User user) {
        if (user == null) {
            return false;
        }
        
        return user.isActive();
    }
}

