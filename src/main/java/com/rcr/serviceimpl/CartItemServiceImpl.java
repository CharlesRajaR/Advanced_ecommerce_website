package com.rcr.serviceimpl;

import com.rcr.model.CartItems;
import com.rcr.repository.CartItemRepository;
import com.rcr.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    @Override
    public CartItems updateCartItem(Long cartItemId, int quantity) throws Exception {
        CartItems cartItems = getCartItemById(cartItemId);

        cartItems.setQuantity(quantity);

        return cartItemRepository.save(cartItems);
    }

    @Override
    public CartItems getCartItemById(Long id) throws Exception {
        CartItems cartItems = cartItemRepository.findById(id)
                .orElseThrow(() -> new Exception("cart item not found..."));

        return cartItems;
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}
