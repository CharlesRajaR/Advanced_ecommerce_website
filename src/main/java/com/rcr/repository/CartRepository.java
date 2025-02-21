package com.rcr.repository;

import com.rcr.model.Cart;
import com.rcr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
