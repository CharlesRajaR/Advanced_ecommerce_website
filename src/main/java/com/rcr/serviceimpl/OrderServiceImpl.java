package com.rcr.serviceimpl;

import com.rcr.domain.OrderStatus;
import com.rcr.domain.PaymentStatus;
import com.rcr.model.*;
import com.rcr.repository.AddressRepository;
import com.rcr.repository.OrderItemRepository;
import com.rcr.repository.OrderRepository;
import com.rcr.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if(!user.getAddresses().contains(shippingAddress)){
            addressRepository.save(shippingAddress);
        }

        Map<Long, List<CartItems>> map = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
        Set<Order> order = new HashSet<>();

        for(Map.Entry<Long, List<CartItems>> entry : map.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItems> items = entry.getValue();
            int totalItems = items.stream().mapToInt(CartItems::getQuantity).sum();


            Order order1 = new Order();
            order1.setUser(user);
            order1.setTotalItem(totalItems);
            order1.setSellerId(sellerId);
            order1.setShippingAddress(shippingAddress);
            order1.setOrderStatus(OrderStatus.PENDING);
            order1.setPaymentStatus(PaymentStatus.PENDING);
            Order saved = orderRepository.save(order1);

            List<OrderItem> orderItems = new ArrayList<>();
            int totalSP = 0;
            int totalMRP = 0;
            for(CartItems item : items){
                OrderItem orderItem = new OrderItem();
                orderItem.setMrp(item.getMrp());
                totalMRP += item.getMrp();
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSellingPrice(item.getSellingPrice());
                totalSP += item.getSellingPrice();
                orderItem.setProduct(item.getProduct());
                orderItem.setUserId(user.getId());
                orderItem.setOrder(saved);


                orderItems.add(orderItemRepository.save(orderItem));
            }

            saved.setOrderItems(orderItems);
            saved.setTotalSellingPrice(totalSP);
            saved.setTotalMrp(totalMRP);

            order.add(orderRepository.save(saved));
        }

        return order;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("order not found..."));
        return order;
    }

    @Override
    public List<Order> findUserOrder(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findSellerOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);

        return orderRepository.save(order);
    }

    @Override
    public Order updatePaymentStatus(Long orderId, PaymentStatus status) throws Exception {
        Order order = findOrderById(orderId);
        order.setPaymentStatus(status);

        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {

        Order order = findOrderById(orderId);
        if(!order.getUser().equals(user)) throw new Exception("you don't have access to cancel...");

        order.setOrderStatus(OrderStatus.ORDER_CANCELED);

        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new Exception("order item not found"));
        return orderItem;
    }
}
