package com.rcr.controller;

import com.rcr.domain.OrderStatus;
import com.rcr.model.Order;
import com.rcr.model.Seller;
import com.rcr.service.OrderService;
import com.rcr.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller-api")
public class SellerOrderController {
    private final OrderService orderService;
    private final SellerService sellerService;
    @GetMapping("/get/seller-order")
    public ResponseEntity<List<Order>> getSellerOrders(@RequestHeader("Authorization") String jwt){
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.findSellerOrder(seller.getId());

        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/update/order-status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId,
                                                   @PathVariable OrderStatus status) throws Exception {
        Order order = orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(order);
    }
}
