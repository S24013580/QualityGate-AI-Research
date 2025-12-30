package com.qualitygate.research.controller;

import com.qualitygate.research.domain.User;
import com.qualitygate.research.service.UserService;
import java.util.ArrayList;
import java.util.List;

/**
 * REST Controller for User operations
 * Handles HTTP requests and delegates to UserService
 */
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("UserService cannot be null");
        }
        this.userService = userService;
    }
    
    /**
     * Creates a new user
     * @param username The username
     * @param email The email address
     * @return Created user or null if validation fails
     */
    public User createUser(String username, String email) {
        if (username == null || email == null) {
            return null;
        }
        return userService.createUser(username, email);
    }
    
    /**
     * Updates user email
     * @param user The user to update
     * @param newEmail The new email address
     * @return true if update successful, false otherwise
     */
    public boolean updateUserEmail(User user, String newEmail) {
        if (user == null || newEmail == null) {
            return false;
        }
        return userService.updateUserEmail(user, newEmail);
    }
    
    /**
     * Activates a user account
     * @param user The user to activate
     * @return true if activation successful, false otherwise
     */
    public boolean activateUser(User user) {
        if (user == null) {
            return false;
        }
        return userService.activateUser(user);
    }
    
    /**
     * Deactivates a user account
     * @param user The user to deactivate
     * @return true if deactivation successful, false otherwise
     */
    public boolean deactivateUser(User user) {
        if (user == null) {
            return false;
        }
        return userService.deactivateUser(user);
    }
    
    /**
     * Checks if a user is active
     * @param user The user to check
     * @return true if user is active, false otherwise
     */
    public boolean isUserActive(User user) {
        if (user == null) {
            return false;
        }
        return userService.isUserActive(user);
    }
    
    /**
     * Validates email address
     * @param email The email to validate
     * @return true if email is valid, false otherwise
     */
    public boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        return userService.isValidEmail(email);
    }
    
    /**
     * Validates username
     * @param username The username to validate
     * @return true if username is valid, false otherwise
     */
    public boolean validateUsername(String username) {
        if (username == null) {
            return false;
        }
        return userService.isValidUsername(username);
    }
}

