package com.rcr.controller;

import com.rcr.customReq.ReviewReq;
import com.rcr.model.Product;
import com.rcr.model.Review;
import com.rcr.model.User;
import com.rcr.service.ProductService;
import com.rcr.service.ReviewService;
import com.rcr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserService userService;
    @GetMapping("/product-review/{productId}")
    public ResponseEntity<List<Review>> getProductReview(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable Long productId){
        Product product = productService.findById(productId);
        List<Review> reviews = reviewService.getProductReview(product);

        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/create-review/{productId}")
    public ResponseEntity<Review> createProductReview(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable Long productId,
                                                      @RequestBody ReviewReq req) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findById(productId);
        Review reviews = reviewService.createReview(user, product, req);

        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/update-review/{reviewId}")
    public ResponseEntity<Review> updateProductReview(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable Long reviewId,
                                                      @RequestBody ReviewReq req) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Review reviews = reviewService.updateReview(reviewId, req.getReviewText(),
                req.getProductImages(), user.getId(), req.getRatings());

        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/delete-review/{reviewId}")
    public ResponseEntity<String> deleteReview(@RequestHeader("Authorization") String jwt,
                                               @PathVariable Long reviewId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(user.getId(), reviewId);
        return ResponseEntity.ok("review deleted successfully");
    }
}
