package com.rcr.controller;


import com.razorpay.PaymentLink;
import com.rcr.domain.PaymentLinkResponse;
import com.rcr.domain.PaymentMethod;
import com.rcr.model.*;
import com.rcr.repository.PaymentOrderRepository;
import com.rcr.service.CartService;
import com.rcr.service.OrderService;
import com.rcr.service.PaymentService;
import com.rcr.service.UserService;
import jakarta.mail.search.SearchTerm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserOrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final CartService cartService;
    private final PaymentOrderRepository paymentOrderRepository;
    @PostMapping("/create-order")
    public ResponseEntity<PaymentLinkResponse> createOrder(@RequestHeader("Authorization") String jwt,
                                                           @RequestBody Address shippingAddress,
                                                           @RequestParam PaymentMethod paymentMethod) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Order> order = orderService.createOrder(user, shippingAddress, cart);

        PaymentLinkResponse res = new PaymentLinkResponse();
        PaymentOrder paymentOrder = paymentService.createOrder(user, order);

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink paymentLink = paymentService.createRazorpayPaymentLink(
                    user, paymentOrder.getId(), paymentOrder.getAmount()
            );
            String paymentUrl = paymentLink.get("short_url");
            String paymentId = paymentLink.get("id");

            res.setGetPayment_link_id(paymentId);
            res.setPayment_link_url(paymentUrl);

            paymentOrder.setPaymentLinkId(paymentId);
            paymentOrderRepository.save(paymentOrder);
        }
        else{
            String paymentUrl = paymentService.createStripePaymentLink(user, paymentOrder.getId(),
                    paymentOrder.getAmount());

            res.setPayment_link_url(paymentUrl);
        }

        return new ResponseEntity(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/get/user-order")
    public ResponseEntity<List<Order>> userOrderHistory(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.findUserOrder(user.getId());

        return ResponseEntity.ok(orders);
    }
    @GetMapping("/get-single-order/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) throws Exception {

        Order order = orderService.findOrderById(orderId);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/order-item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId) throws Exception {
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);

        return ResponseEntity.ok(orderItem);
    }

    @PatchMapping("/cancel-order/{orderId}")
    public ResponseEntity<Order> cancelOrder(@RequestHeader("Authorization") String jwt,
                                              @PathVariable Long orderId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }
}
