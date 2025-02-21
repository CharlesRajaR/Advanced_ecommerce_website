package com.rcr.repository;

import com.rcr.model.Product;
import com.rcr.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {
    public List<Product> findBySellerId(Long id);

//    public List<Product> searchProduct(String query);
}
