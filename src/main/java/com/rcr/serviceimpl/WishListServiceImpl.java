package com.rcr.serviceimpl;

import com.rcr.model.Product;
import com.rcr.model.User;
import com.rcr.model.WishList;
import com.rcr.repository.UserRepository;
import com.rcr.repository.WishListRepository;
import com.rcr.service.UserService;
import com.rcr.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    @Override
    public WishList createWishList(User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);
        return wishListRepository.save(wishList);
    }

    @Override
    public WishList getWishListByUserId(Long id) throws Exception {

        WishList wishList = wishListRepository.findByUserId(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("user not found to create wishList"));
        if(wishList == null){
            wishList = createWishList(user);
        }
        return wishList;
    }

    @Override
    public WishList addProduct(User user, Product product) throws Exception {
        WishList wishList = getWishListByUserId(user.getId());
        wishList.getProducts().add(product);

        return wishListRepository.save(wishList);
    }
}
