package com.rcr.repository;

import com.rcr.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    public List<Review> findByProductId(Long productId);
}
