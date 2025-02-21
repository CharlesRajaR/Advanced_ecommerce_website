package com.rcr.service;


import com.rcr.model.Product;
import com.rcr.model.User;
import com.rcr.model.WishList;

public interface WishListService {
    public WishList createWishList(User user);
    public WishList getWishListByUserId(Long id) throws Exception;
    public WishList addProduct(User user, Product product) throws Exception;

}
