package com.rcr.service;

import com.rcr.model.Cart;
import com.rcr.model.CartItems;
import com.rcr.model.Product;
import com.rcr.model.User;

import java.util.List;

public interface CartService {
    public CartItems addCartItem(User user, Product product, int quantity, String size);
    public Cart findUserCart(User user);
}
