package com.rcr.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ElementCollection
    @Column(length = 1000)
    private List<String> images;
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Integer quantity;
    private String color;
    private String sizes;
    private String brand;
    private Integer markedPrice;
    private Integer sellingPrice;
    private Double discount;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
