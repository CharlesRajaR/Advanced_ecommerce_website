package com.rcr.model;

import com.rcr.domain.OrderStatus;
import com.rcr.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Long sellerId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    @Embedded
    private PaymentDetails paymentDetails;
    private Integer totalMrp;
    private Integer totalSellingPrice;
    private Integer discount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;
    private Integer totalItem;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private LocalDateTime orderDate = LocalDateTime.now();
    private LocalDateTime deliveredDate = orderDate.plusDays(7);
}
