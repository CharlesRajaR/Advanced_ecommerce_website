package com.rcr.model;

import com.rcr.domain.PaymentMethod;
import com.rcr.domain.PaymentOrderStatus;
import com.rcr.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long amount;
    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;
    private PaymentMethod paymentMethod;
    private String paymentLinkId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    private Set<Order> orders = new HashSet<>();
}
