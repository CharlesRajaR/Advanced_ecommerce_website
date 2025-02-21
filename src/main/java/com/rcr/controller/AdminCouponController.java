package com.rcr.controller;

import com.rcr.model.Cart;
import com.rcr.model.Coupon;
import com.rcr.model.User;
import com.rcr.service.CouponService1;
import com.rcr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AdminCouponController {
    private final CouponService1 couponService1;
    private final UserService userService;
    @PutMapping("/apply-coupon")
    public ResponseEntity<Cart> applyCoupon(@RequestHeader("Authorization") String jwt,
                                            @RequestParam String apply,
                                            @RequestParam String code,
                                            @RequestParam double orderValue) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;
        if(apply.equals("true")) cart = couponService1.applyCoupon(user, code, orderValue);
       else{
          cart = couponService1.removeCoupon(code, user);
       }

       return ResponseEntity.ok(cart);
    }

    @PostMapping("/admin/create-coupon")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){
        Coupon coupon1 = couponService1.createCoupon(coupon);
         return ResponseEntity.ok(coupon1);
    }

    @DeleteMapping("/admin/delete-coupon/{couponId}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long couponId){
        couponService1.deleteCoupon(couponId);
        return ResponseEntity.ok("coupon deleted successfully...");
    }

    @GetMapping("/admin/get-all-coupon")
    public ResponseEntity<List<Coupon>> getAllCoupon(){
        List<Coupon> coupon1 = couponService1.findAllCoupon();
        return ResponseEntity.ok(coupon1);
    }
}
