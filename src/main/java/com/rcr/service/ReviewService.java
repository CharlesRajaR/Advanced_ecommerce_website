package com.rcr.service;

import com.rcr.customReq.ReviewReq;
import com.rcr.model.Product;
import com.rcr.model.Review;
import com.rcr.model.User;

import java.util.List;

public interface ReviewService {
    public Review createReview(User user, Product product, ReviewReq req);
    public List<Review> getProductReview(Product product);
    public Review updateReview(Long reviewId, String text, List<String> images, Long userId, double rating) throws Exception;
    public void deleteReview(Long id, Long reviewId) throws Exception;
}
