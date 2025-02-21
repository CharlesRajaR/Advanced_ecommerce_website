package com.rcr.service;

import com.rcr.model.Cart;
import com.rcr.model.Coupon;
import com.rcr.model.User;

import java.util.List;

public interface CouponService1 {
     Cart applyCoupon(User user, String code, double orderValue) throws Exception;
     Cart removeCoupon(String code, User user);

     Coupon findCouponById(Long id) throws Exception;
     Coupon createCoupon(Coupon coupon);

     List<Coupon> findAllCoupon();

     void deleteCoupon(Long couponId);
}
