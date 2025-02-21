package com.rcr.service;

import com.rcr.customReq.CreateProductRequest;
import com.rcr.model.Product;
import com.rcr.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(Seller seller, CreateProductRequest req);
    public Product updateProduct(Long id, Product product) throws Exception;
    public Product findById(Long id);
    public void deleteProduct(Long id);
    public List<Product> searchProduct(String query);
    public Page<Product> getAllProduct(
            String category,
            String brand,
            String color,
            String sizes,
            String minPrice,
            String maxPrice,
            String sort,
            String stock,
            Integer pageNumber,
            Integer minDiscount
    );
    public List<Product> getProductBySellerId(Long sellerId) throws Exception;
}
