package com.rcr.serviceimpl;

import com.rcr.customReq.ReviewReq;
import com.rcr.model.Product;
import com.rcr.model.Review;
import com.rcr.model.User;
import com.rcr.repository.ReviewRepository;
import com.rcr.repository.UserRepository;
import com.rcr.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    @Override
    public Review createReview(User user, Product product, ReviewReq req) {
        Review review = new Review();
        review.setReviewText(req.getReviewText());
        review.setUser(user);
        review.setProduct(product);
        review.setProductImages(req.getProductImages());
        review.setRatings(req.getRatings());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getProductReview(Product product) {
        return reviewRepository.findByProductId(product.getId());
    }

    @Override
    public Review updateReview(Long reviewId, String text, List<String> images, Long userId, double rating) throws Exception {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception("reviw not found..."));
        if(text != null) review.setReviewText(text);
        if(images.size() != 0) review.getProductImages().addAll(images);
        if(rating < 0) review.setRatings(rating);

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id, Long reviewId) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("user not found"));
        Review review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new Exception("review not found"));
        if(!user.getId().equals(review.getUser().getId())) throw new Exception("you don't have access");
        reviewRepository.deleteById(reviewId);
    }
}
