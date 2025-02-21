package com.rcr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
    private Long totalEarnings = 0L;
    private Long totalSales = 0L;
    private Long totalRefunds = 0L;
    private Long totalTax;
    private Long netEarnings = 0L;
    private Integer totalOrders = 0;
    private Integer canceledOrders = 0;
    private Integer totalTransactions = 0;
}
