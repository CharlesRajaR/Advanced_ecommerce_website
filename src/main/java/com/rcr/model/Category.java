package com.rcr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    private String categoryId;
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Category parentCategory;
    @NotNull
    private Integer level;
}
