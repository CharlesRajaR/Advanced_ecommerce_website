package com.rcr.serviceimpl;

import com.rcr.model.Cart;
import com.rcr.model.CartItems;
import com.rcr.model.Product;
import com.rcr.model.User;
import com.rcr.repository.CartItemRepository;
import com.rcr.repository.CartRepository;
import com.rcr.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItems addCartItem(User user, Product product, int quantity, String size) {
        Cart cart = cartRepository.findByUser(user);

        CartItems isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);

        if(isPresent != null) return isPresent;

        CartItems cartItems = new CartItems();
        cartItems.setCart(cart);
        cartItems.setProduct(product);
        cartItems.setQuantity(quantity);
        cartItems.setSize(size);
        cartItems.setMrp(quantity * product.getMarkedPrice());
        cartItems.setSellingPrice(quantity * product.getSellingPrice());
        cartItems.setUserId(user.getId());

        CartItems saved = cartItemRepository.save(cartItems);
        cart.getCartItems().add(saved);

        return saved;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUser(user);
        int totalMRP = 0;
        int totalSP = 0;
        int totalItems = 0;

        for(CartItems item : cart.getCartItems()){
            totalItems += item.getQuantity();
            totalMRP += item.getMrp();
            totalSP += item.getSellingPrice();
        }

        cart.setTotalItem(totalItems);
        cart.setTotalMrpPrice(totalMRP);
        cart.setTotalSellingPrice((double)totalSP);

        cartRepository.save(cart);

        return cart;
    }
}
