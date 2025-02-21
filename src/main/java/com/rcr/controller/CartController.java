package com.rcr.controller;

import com.rcr.customReq.CartItemReq;
import com.rcr.model.Cart;
import com.rcr.model.CartItems;
import com.rcr.model.Product;
import com.rcr.model.User;
import com.rcr.service.CartItemService;
import com.rcr.service.CartService;
import com.rcr.service.ProductService;
import com.rcr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;
    @GetMapping("/user-cart")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add-item/to-cart")
    public ResponseEntity<CartItems> addItemToCart(@RequestHeader("Authorization") String jwt,
                                                   CartItemReq req) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findById(req.getProductId());

        CartItems cartItems = cartService.addCartItem(user, product, req.getQuantity(), req.getSize());


        return new ResponseEntity(cartItems, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/update/cart-item")
    public ResponseEntity<CartItems> updateCartItem(@RequestHeader("Authorization") String jwt,
                                                    @RequestParam  Long cartItemId,
                                                    @RequestParam int quantity) throws Exception {
       CartItems cartItems = cartItemService.updateCartItem(cartItemId, quantity);

       return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/delete/cart-item/{id}")
    public ResponseEntity<String> deleteCartItem(@RequestHeader("Authorization") String jwt,
                                                 @PathVariable Long id) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        if(!user.getId().equals(id)){
            throw new Exception("you do not delete this item...");
        }

        cartItemService.deleteCartItem(id);

        return ResponseEntity.ok("cart item deleted with id "+ id);
    }

}
