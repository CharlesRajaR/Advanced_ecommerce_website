package com.rcr.controller;

import com.rcr.model.Product;
import com.rcr.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserProductController {
    private final ProductService productService;
    @GetMapping("/get/all/products")
    public ResponseEntity<Page<Product>> getAllProduct(@RequestParam(required = false) String category,
                                                       @RequestParam(required = false) String brand,
                                                       @RequestParam(required = false) String color,
                                                       @RequestParam(required = false) String sizes,
                                                       @RequestParam(required = false) String minPrice,
                                                       @RequestParam(required = false) String maxPrice,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam(required = false) String stock,
                                                       @RequestParam(required = false) Integer pageNumber,
                                                       @RequestParam(required = false) Integer minDiscount)throws Exception{
        Page<Product> products = productService.getAllProduct(category,brand,color,sizes,minPrice, maxPrice,
                sort, stock, pageNumber, minDiscount);


        return ResponseEntity.ok(products);
    }

    @GetMapping("/get-product/{id}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable Long id)throws Exception{
        Product product = productService.findById(id);

        return ResponseEntity.ok(product);
    }

    @GetMapping("/search/product")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String query){
        List<Product> products = productService.searchProduct(query);

        return ResponseEntity.ok(products);
    }
}
