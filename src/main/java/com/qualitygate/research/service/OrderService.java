package com.qualitygate.research.service;

import com.qualitygate.research.domain.Order;
import com.qualitygate.research.domain.OrderItem;
import com.qualitygate.research.config.DiscountConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service Layer - OrderService
 * 
 * Contains all pricing and discount logic. This is modeled after real-world enterprise services
 * where a number of business rules need to be applied in a specific sequence.
 * 
 * Key design elements:
 * - Stateless Service Methods
 * - Explicit parameter validation
 * - Clear separation of calculation steps
 * - Absolutely no side effects or external dependencies
 * 
 * This allows for deterministic behavior and is necessary for performing unit testing and mutation testing.
 */
public class OrderService {
    
    private final DiscountConfiguration discountConfig;
    
    public OrderService(DiscountConfiguration discountConfig) {
        if (discountConfig == null) {
            throw new IllegalArgumentException("DiscountConfiguration cannot be null");
        }
        this.discountConfig = discountConfig;
    }
    
    /**
     * Calculates the total price for an order including all discounts.
     * This method applies business rules in a specific sequence:
     * 1. Calculate subtotal
     * 2. Apply volume discount
     * 3. Apply customer tier discount
     * 4. Apply promotional discount
     * 5. Calculate final total
     * 
     * @param order The order to calculate pricing for
     * @return The calculated order with all amounts set
     * @throws IllegalArgumentException if order is null or invalid
     */
    public Order calculateOrderTotal(Order order) {
        // Explicit parameter validation
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        
        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        
        // Step 1: Calculate subtotal
        BigDecimal subtotal = calculateSubtotal(items);
        order.setSubtotal(subtotal);
        
        // Step 2: Apply volume discount
        BigDecimal volumeDiscount = calculateVolumeDiscount(items, subtotal);
        
        // Step 3: Apply customer tier discount (if applicable)
        BigDecimal customerDiscount = calculateCustomerTierDiscount(subtotal, order.getCustomerId());
        
        // Step 4: Apply promotional discount (if applicable)
        BigDecimal promotionalDiscount = calculatePromotionalDiscount(subtotal);
        
        // Step 5: Determine maximum discount (business rule: only one discount type applies)
        BigDecimal maxDiscount = volumeDiscount.max(customerDiscount).max(promotionalDiscount);
        
        // Step 6: Apply discount cap (if configured)
        BigDecimal discountAmount = applyDiscountCap(maxDiscount, subtotal);
        order.setDiscountAmount(discountAmount);
        
        // Step 7: Calculate final total
        BigDecimal totalAmount = subtotal.subtract(discountAmount);
        order.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        
        return order;
    }
    
    /**
     * Calculates the subtotal by summing all line items.
     * Clear separation of calculation steps.
     */
    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (OrderItem item : items) {
            if (item == null) {
                throw new IllegalArgumentException("Order item cannot be null");
            }
            
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be greater than zero");
            }
            
            if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Item unit price must be non-negative");
            }
            
            BigDecimal lineTotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            
            item.setLineTotal(lineTotal);
            subtotal = subtotal.add(lineTotal);
        }
        
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculates volume discount based on total quantity.
     * Business rule: Higher quantities get better discounts.
     */
    private BigDecimal calculateVolumeDiscount(List<OrderItem> items, BigDecimal subtotal) {
        int totalQuantity = items.stream()
                .mapToInt(item -> item.getQuantity() != null ? item.getQuantity() : 0)
                .sum();
        
        BigDecimal discountRate = BigDecimal.ZERO;
        
        if (totalQuantity >= discountConfig.getVolumeDiscountTier3Threshold()) {
            discountRate = discountConfig.getVolumeDiscountTier3Rate();
        } else if (totalQuantity >= discountConfig.getVolumeDiscountTier2Threshold()) {
            discountRate = discountConfig.getVolumeDiscountTier2Rate();
        } else if (totalQuantity >= discountConfig.getVolumeDiscountTier1Threshold()) {
            discountRate = discountConfig.getVolumeDiscountTier1Rate();
        }
        
        return subtotal.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculates customer tier discount based on customer ID.
     * Business rule: Premium customers get additional discount.
     */
    private BigDecimal calculateCustomerTierDiscount(BigDecimal subtotal, Long customerId) {
        if (customerId == null) {
            return BigDecimal.ZERO;
        }
        
        // Business rule: Customer IDs divisible by 100 are premium customers
        boolean isPremiumCustomer = (customerId % 100) == 0;
        
        if (isPremiumCustomer) {
            return subtotal.multiply(discountConfig.getPremiumCustomerDiscountRate())
                    .setScale(2, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Calculates promotional discount based on order value.
     * Business rule: Orders above certain threshold get promotional discount.
     */
    private BigDecimal calculatePromotionalDiscount(BigDecimal subtotal) {
        if (subtotal.compareTo(discountConfig.getPromotionalDiscountThreshold()) >= 0) {
            return subtotal.multiply(discountConfig.getPromotionalDiscountRate())
                    .setScale(2, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Applies discount cap if configured.
     * Business rule: Maximum discount cannot exceed configured percentage of subtotal.
     */
    private BigDecimal applyDiscountCap(BigDecimal discount, BigDecimal subtotal) {
        BigDecimal maxAllowedDiscount = subtotal.multiply(discountConfig.getMaxDiscountRate());
        
        if (discount.compareTo(maxAllowedDiscount) > 0) {
            return maxAllowedDiscount.setScale(2, RoundingMode.HALF_UP);
        }
        
        return discount;
    }
    
    /**
     * Validates order before processing.
     * Explicit parameter validation as per design.
     */
    public boolean validateOrder(Order order) {
        if (order == null) {
            return false;
        }
        
        if (order.getCustomerId() == null || order.getCustomerId() <= 0) {
            return false;
        }
        
        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) {
            return false;
        }
        
        for (OrderItem item : items) {
            if (item == null) {
                return false;
            }
            
            if (item.getProductId() == null || item.getProductId().trim().isEmpty()) {
                return false;
            }
            
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                return false;
            }
            
            if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        }
        
        return true;
    }
}

