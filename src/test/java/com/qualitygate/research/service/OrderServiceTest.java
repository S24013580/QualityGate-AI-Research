package com.qualitygate.research.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.qualitygate.research.domain.Order;
import com.qualitygate.research.domain.OrderItem;
import com.qualitygate.research.config.DiscountConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceTest {

    private OrderService orderService;
    private DiscountConfiguration discountConfig;

    @BeforeEach
    void setUp() {
        discountConfig = mock(DiscountConfiguration.class);
        when(discountConfig.getVolumeDiscountTier1Threshold()).thenReturn(10);
        when(discountConfig.getVolumeDiscountTier2Threshold()).thenReturn(20);
        when(discountConfig.getVolumeDiscountTier3Threshold()).thenReturn(30);
        when(discountConfig.getVolumeDiscountTier1Rate()).thenReturn(new BigDecimal("0.05"));
        when(discountConfig.getVolumeDiscountTier2Rate()).thenReturn(new BigDecimal("0.10"));
        when(discountConfig.getVolumeDiscountTier3Rate()).thenReturn(new BigDecimal("0.15"));
        when(discountConfig.getPremiumCustomerDiscountRate()).thenReturn(new BigDecimal("0.10"));
        when(discountConfig.getPromotionalDiscountThreshold()).thenReturn(new BigDecimal("100.00"));
        when(discountConfig.getPromotionalDiscountRate()).thenReturn(new BigDecimal("0.05"));
        when(discountConfig.getMaxDiscountRate()).thenReturn(new BigDecimal("0.20"));

        orderService = new OrderService(discountConfig);
    }

    @Test
    void testCalculateOrderTotal_HappyPath() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("Product1", 5, new BigDecimal("20.00")));
        items.add(new OrderItem("Product2", 10, new BigDecimal("10.00")));
        order.setItems(items);

        Order result = orderService.calculateOrderTotal(order);

        assertNotNull(result);
        assertEquals(new BigDecimal("200.00"), result.getSubtotal());
        assertEquals(new BigDecimal("20.00"), result.getDiscountAmount());
        assertEquals(new BigDecimal("180.00"), result.getTotalAmount());
    }

    @Test
    void testCalculateOrderTotal_NullOrder() {
        assertThrows(IllegalArgumentException.class, () -> orderService.calculateOrderTotal(null));
    }

    @Test
    void testCalculateOrderTotal_EmptyItems() {
        Order order = new Order(1L, 100L);
        order.setItems(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> orderService.calculateOrderTotal(order));
    }

    @Test
    void testCalculateOrderTotal_NullItem() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(null);
        order.setItems(items);

        assertThrows(IllegalArgumentException.class, () -> orderService.calculateOrderTotal(order));
    }

    @Test
    void testCalculateOrderTotal_InvalidQuantity() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("Product1", 0, new BigDecimal("20.00")));
        order.setItems(items);

        assertThrows(IllegalArgumentException.class, () -> orderService.calculateOrderTotal(order));
    }

    @Test
    void testCalculateOrderTotal_InvalidUnitPrice() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("Product1", 5, new BigDecimal("-20.00")));
        order.setItems(items);

        assertThrows(IllegalArgumentException.class, () -> orderService.calculateOrderTotal(order));
    }

    @Test
    void testValidateOrder_HappyPath() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("Product1", 5, new BigDecimal("20.00")));
        order.setItems(items);

        assertTrue(orderService.validateOrder(order));
    }

    @Test
    void testValidateOrder_NullOrder() {
        assertFalse(orderService.validateOrder(null));
    }

    @Test
    void testValidateOrder_NullCustomerId() {
        Order order = new Order();
        order.setItems(new ArrayList<>());

        assertFalse(orderService.validateOrder(order));
    }

    @Test
    void testValidateOrder_EmptyItems() {
        Order order = new Order(1L, 100L);
        order.setItems(new ArrayList<>());

        assertFalse(orderService.validateOrder(order));
    }

    @Test
    void testValidateOrder_NullItem() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(null);
        order.setItems(items);

        assertFalse(orderService.validateOrder(order));
    }

    @Test
    void testValidateOrder_InvalidProductId() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("", 5, new BigDecimal("20.00")));
        order.setItems(items);

        assertFalse(orderService.validateOrder(order));
    }

    @Test
    void testValidateOrder_InvalidQuantity() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("Product1", 0, new BigDecimal("20.00")));
        order.setItems(items);

        assertFalse(orderService.validateOrder(order));
    }

    @Test
    void testValidateOrder_InvalidUnitPrice() {
        Order order = new Order(1L, 100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("Product1", 5, new BigDecimal("-20.00")));
        order.setItems(items);

        assertFalse(orderService.validateOrder(order));
    }
}