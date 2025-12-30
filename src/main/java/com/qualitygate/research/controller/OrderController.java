package com.qualitygate.research.controller;

import com.qualitygate.research.domain.Order;
import com.qualitygate.research.service.OrderService;
import java.util.List;

/**
 * REST Controller for Order operations
 * Handles HTTP requests and delegates to OrderService
 */
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        if (orderService == null) {
            throw new IllegalArgumentException("OrderService cannot be null");
        }
        this.orderService = orderService;
    }
    
    /**
     * Calculates order total with all discounts applied
     * @param order The order to calculate
     * @return Order with calculated totals, or null if order is invalid
     */
    public Order calculateOrderTotal(Order order) {
        if (order == null) {
            return null;
        }
        
        if (!orderService.validateOrder(order)) {
            return null;
        }
        
        try {
            return orderService.calculateOrderTotal(order);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Validates an order
     * @param order The order to validate
     * @return true if order is valid, false otherwise
     */
    public boolean validateOrder(Order order) {
        if (order == null) {
            return false;
        }
        return orderService.validateOrder(order);
    }
    
    /**
     * Processes an order (validates and calculates total)
     * @param order The order to process
     * @return Processed order with totals, or null if invalid
     */
    public Order processOrder(Order order) {
        if (order == null) {
            return null;
        }
        
        if (!validateOrder(order)) {
            return null;
        }
        
        return calculateOrderTotal(order);
    }
}

