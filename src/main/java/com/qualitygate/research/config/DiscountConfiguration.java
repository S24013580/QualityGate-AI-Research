package com.qualitygate.research.config;

import java.math.BigDecimal;

/**
 * Configuration Layer - DiscountConfiguration
 * 
 * Application configuration management for discount rules.
 * This is a stateless configuration class that provides discount parameters.
 */
public class DiscountConfiguration {
    
    // Volume discount thresholds and rates
    private final int volumeDiscountTier1Threshold;
    private final int volumeDiscountTier2Threshold;
    private final int volumeDiscountTier3Threshold;
    private final BigDecimal volumeDiscountTier1Rate;
    private final BigDecimal volumeDiscountTier2Rate;
    private final BigDecimal volumeDiscountTier3Rate;
    
    // Premium customer discount
    private final BigDecimal premiumCustomerDiscountRate;
    
    // Promotional discount
    private final BigDecimal promotionalDiscountThreshold;
    private final BigDecimal promotionalDiscountRate;
    
    // Maximum discount cap
    private final BigDecimal maxDiscountRate;
    
    public DiscountConfiguration() {
        // Default configuration values
        this.volumeDiscountTier1Threshold = 10;
        this.volumeDiscountTier2Threshold = 50;
        this.volumeDiscountTier3Threshold = 100;
        this.volumeDiscountTier1Rate = new BigDecimal("0.05");  // 5%
        this.volumeDiscountTier2Rate = new BigDecimal("0.10"); // 10%
        this.volumeDiscountTier3Rate = new BigDecimal("0.15"); // 15%
        
        this.premiumCustomerDiscountRate = new BigDecimal("0.20"); // 20%
        
        this.promotionalDiscountThreshold = new BigDecimal("500.00");
        this.promotionalDiscountRate = new BigDecimal("0.10"); // 10%
        
        this.maxDiscountRate = new BigDecimal("0.30"); // Maximum 30% discount
    }
    
    // Getters
    public int getVolumeDiscountTier1Threshold() {
        return volumeDiscountTier1Threshold;
    }
    
    public int getVolumeDiscountTier2Threshold() {
        return volumeDiscountTier2Threshold;
    }
    
    public int getVolumeDiscountTier3Threshold() {
        return volumeDiscountTier3Threshold;
    }
    
    public BigDecimal getVolumeDiscountTier1Rate() {
        return volumeDiscountTier1Rate;
    }
    
    public BigDecimal getVolumeDiscountTier2Rate() {
        return volumeDiscountTier2Rate;
    }
    
    public BigDecimal getVolumeDiscountTier3Rate() {
        return volumeDiscountTier3Rate;
    }
    
    public BigDecimal getPremiumCustomerDiscountRate() {
        return premiumCustomerDiscountRate;
    }
    
    public BigDecimal getPromotionalDiscountThreshold() {
        return promotionalDiscountThreshold;
    }
    
    public BigDecimal getPromotionalDiscountRate() {
        return promotionalDiscountRate;
    }
    
    public BigDecimal getMaxDiscountRate() {
        return maxDiscountRate;
    }
}

