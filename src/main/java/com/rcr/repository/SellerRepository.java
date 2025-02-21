package com.rcr.repository;

import com.rcr.domain.AccountStatus;
import com.rcr.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    public Seller findByEmail(String email);
    public List<Seller> findByAccountStatus(AccountStatus status);
}
