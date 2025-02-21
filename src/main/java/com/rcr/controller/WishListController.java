package com.rcr.controller;

import com.rcr.model.Product;
import com.rcr.model.User;
import com.rcr.model.WishList;
import com.rcr.service.ProductService;
import com.rcr.service.UserService;
import com.rcr.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;
    @GetMapping("/get-wish-list")
    public ResponseEntity<WishList> getWishListByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        WishList wishList = wishListService.getWishListByUserId(user.getId());

        return ResponseEntity.ok(wishList);
    }

    @PutMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductTotWishList(@RequestHeader("Authorization") String jwt
    ,@PathVariable Long productId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Product product = productService.findById(productId);

        WishList wishList = wishListService.addProduct(user, product);

        return ResponseEntity.ok(wishList);
    }
}
