package com.rcr.service;

import com.rcr.domain.OrderStatus;
import com.rcr.domain.PaymentStatus;
import com.rcr.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    public Order findOrderById(Long id) throws Exception;
    public List<Order> findUserOrder(Long userId);
    public List<Order> findSellerOrder(Long sellerId);
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
    public Order updatePaymentStatus(Long orderId, PaymentStatus status) throws Exception;
    public Order cancelOrder(Long orderId, User user) throws Exception;
    public OrderItem getOrderItemById(Long id) throws Exception;
}
