package com.rcr.service;

import com.rcr.model.Cart;
import com.rcr.model.CartItems;
import com.rcr.model.Product;

public interface CartItemService {
    public CartItems updateCartItem(Long cartItemId, int quantity) throws Exception;
    public CartItems getCartItemById(Long id) throws Exception;
    public void deleteCartItem(Long id);
}
