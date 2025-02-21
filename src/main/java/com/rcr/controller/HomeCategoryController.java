package com.rcr.controller;

import com.rcr.model.Home;
import com.rcr.model.HomeCategory;
import com.rcr.service.HomeCategoryService;
import com.rcr.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeCategoryController {
    private final HomeService homeService;
    private final HomeCategoryService homeCategoryService;
    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories){
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);

        return ResponseEntity.ok(home);
    }

    @GetMapping("/get/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory(){
        return ResponseEntity.ok(homeCategoryService.getAllHomeCategories());
    }

    @PatchMapping("/update-home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id,
                                                           @RequestBody HomeCategory homeCategory) throws Exception {
       HomeCategory updated =  homeCategoryService.updateHomeCategory(homeCategory, id);

       return ResponseEntity.ok(updated);
    }
}
