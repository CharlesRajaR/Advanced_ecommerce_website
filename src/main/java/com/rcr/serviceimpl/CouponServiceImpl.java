package com.rcr.serviceimpl;

import com.rcr.model.Cart;
import com.rcr.model.Coupon;
import com.rcr.model.User;
import com.rcr.repository.CartRepository;
import com.rcr.repository.CouponRepository;
import com.rcr.repository.UserRepository;
import com.rcr.service.CouponService1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService1 {
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    @Override
    public Cart applyCoupon(User user, String code, double orderValue) throws Exception {
        Cart cart = cartRepository.findByUser(user);
        Coupon coupon = couponRepository.findByCode(code);

        if(coupon == null) throw new Exception("coupon not valid...");
        if(user.getUsedCoupons().contains(coupon)){
            throw new Exception("Already used");
        }
        if(orderValue < coupon.getMinimumOrderValue()){
            throw new Exception("minimum order value must be "+coupon.getMinimumOrderValue());
        }
        if(LocalDateTime.now().isAfter(coupon.getValidationStartDate()) &&
        LocalDateTime.now().isBefore(coupon.getValidationEndDate()) ){
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            double discount = (cart.getTotalSellingPrice() * coupon.getDiscountPercent())/100;

            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discount);
            cart.setCouponCode(code);

        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeCoupon(String code, User user) {

        return null;
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id)
                .orElseThrow(()->new Exception("coupon not found"));
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupon() {
        return couponRepository.findAll();
    }

    @Override
    public void deleteCoupon(Long couponId) {
      couponRepository.deleteById(couponId);
    }
}
