package com.rcr.service;

import com.rcr.domain.AccountStatus;
import com.rcr.model.Seller;

import java.util.List;
import java.util.Optional;

public interface SellerService {
    public Seller getSellerProfile(String jwt);
    public Seller createSeller(Seller seller) throws Exception;
    public Seller getSellerById(Long id)throws Exception;
    public Seller getSellerByEmail(String email) throws Exception;
    public List<Seller> getAllSellers(AccountStatus status);
    public Seller updateSeller(Long id, Seller seller) throws Exception;
    public void deleteSeller(Long id) throws Exception;
    public Seller verifyEmail(String email, String otp) throws Exception;
    public Seller updateSellerAccountStatus(AccountStatus status, Long sellerId) throws Exception;
}
