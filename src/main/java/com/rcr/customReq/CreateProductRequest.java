package com.rcr.customReq;

import com.rcr.model.Category;
import com.rcr.model.Seller;
import lombok.Data;

import java.util.List;

@Data
public class CreateProductRequest {
    private String title;
    private String description;
    private String color;
    private String sizes;
    private List<String> images;
    private String category;
    private String category1;
    private String category2;
    private Integer markedPrice;
    private Integer sellingPrice;

}
