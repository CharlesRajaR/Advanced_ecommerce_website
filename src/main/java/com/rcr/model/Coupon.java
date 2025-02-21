package com.rcr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Double discountPercent;
    private LocalDateTime validationStartDate;
    private LocalDateTime validationEndDate;
    private Double minimumOrderValue;

    @ManyToMany(mappedBy = "usedCoupons")
    private Set<User> userUsedCoupon = new HashSet<>();
}
