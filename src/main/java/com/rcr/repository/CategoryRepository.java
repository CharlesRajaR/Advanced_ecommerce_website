package com.rcr.repository;

import com.rcr.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Category findByCategoryId(String categoryId);
}
