package com.rcr.controller;

import com.rcr.configuration.JwtTokenProvider;
import com.rcr.customReq.CreateProductRequest;
import com.rcr.model.Product;
import com.rcr.model.Seller;
import com.rcr.repository.ProductRepository;
import com.rcr.service.ProductService;
import com.rcr.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller-api")
public class SellerProductController {
    private final ProductService productService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SellerService sellerService;

    @PostMapping("/create-product")
    public ResponseEntity<Product> createProduct(@RequestHeader("Authorization") String jwt,
                                                 @RequestBody CreateProductRequest req) throws Exception {
        String email = jwtTokenProvider.findEmailByJwtToken(jwt);
        Seller seller = sellerService.getSellerByEmail(email);
        Product product = productService.createProduct(seller, req);

        return ResponseEntity.ok(product);
    }

    @PutMapping("/update-product")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) throws Exception {
        Product product1 = productService.updateProduct(product.getId(), product);
        return ResponseEntity.ok(product1);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);

        return ResponseEntity.ok("product deleted successfully...");
    }

    @GetMapping("/get/seller-product")
    public ResponseEntity<List<Product>> getSellerProducts(@RequestHeader("Authorization") String jwt) throws Exception {
        String email = jwtTokenProvider.findEmailByJwtToken(jwt);

        List<Product> products = productService.getProductBySellerId(sellerService.getSellerByEmail(email).getId());

        return ResponseEntity.ok(products);
    }

}
