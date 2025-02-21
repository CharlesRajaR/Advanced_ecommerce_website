package com.rcr.serviceimpl;

import com.rcr.customReq.CreateProductRequest;
import com.rcr.model.Category;
import com.rcr.model.Product;
import com.rcr.model.Seller;
import com.rcr.repository.CategoryRepository;
import com.rcr.repository.ProductRepository;
import com.rcr.repository.SellerRepository;
import com.rcr.service.ProductService;
import com.rcr.util.CalculateDiscount;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product createProduct(Seller seller, CreateProductRequest req) {
        Category category = categoryRepository.findByCategoryId(req.getCategory());
        if(category == null){
            category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(1);
            categoryRepository.save(category);
        }

        Category category1 = categoryRepository.findByCategoryId(req.getCategory1());
        if(category1 == null){
            category1 = new Category();
            category1.setCategoryId(req.getCategory1());
            category1.setLevel(2);
            category1.setParentCategory(category);
            categoryRepository.save(category1);
        }

        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
        if(category2 == null){
            category2 = new Category();
            category2.setCategoryId(req.getCategory2());
            category2.setLevel(3);
            category2.setParentCategory(category1);
            categoryRepository.save(category2);
        }


        Product product = new Product();
        product.setCategory(category2);
        product.setColor(req.getColor());
        product.setImages(req.getImages());
        product.setDescription(req.getDescription());
        product.setSeller(seller);
        product.setMarkedPrice(req.getMarkedPrice());
        product.setSellingPrice(req.getSellingPrice());
        product.setTitle(req.getTitle());
        product.setSizes(req.getSizes());
        product.setDiscount(new CalculateDiscount().findDiscount(req.getSellingPrice(), req.getMarkedPrice()));

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product)throws Exception {
        Product product1 = productRepository.findById(id)
                .orElseThrow(() -> new Exception("product not found"));

        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow();
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = findById(id);
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> searchProduct(String query) {
        List<Product> list = new ArrayList<>();
        return list;
    }

    @Override
    public Page<Product> getAllProduct(String category,
                                       String brand,
                                       String color,
                                       String sizes,
                                       String minPrice,
                                       String maxPrice,
                                       String sort,
                                       String stock,
                                       Integer pageNumber, Integer minDiscount) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(category != null){
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if(brand != null){
                predicates.add(criteriaBuilder.equal(root.get("brand"), brand));
            }
            if(color != null){
                predicates.add(criteriaBuilder.equal(root.get("color"), color));
            }
            if(sizes != null){
                predicates.add(criteriaBuilder.equal(root.get("sizes"), sizes));
            }
            if(minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if(maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            if(minDiscount != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discount"), minDiscount));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        if(sort != null && !sort.isEmpty() ){
            pageable = switch (sort){
                case "price_low" -> PageRequest.of(pageNumber != null ? pageNumber:0, 10,
                        Sort.by("sellingPrice").ascending());

                case "price_high" -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.by("sellingPrice").descending());

                default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                        Sort.unsorted());
            };
        }
        else{
            pageable =  PageRequest.of(pageNumber != null ? pageNumber : 0, 10,
                    Sort.unsorted());
        }
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) throws Exception {
//        Seller seller = sellerRepository.findById(sellerId)
//                .orElseThrow(() -> new Exception("seller not found..."));
        return productRepository.findBySellerId(sellerId);
    }
}
