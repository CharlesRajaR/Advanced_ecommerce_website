package com.rcr.customReq;

import lombok.Data;

import java.util.List;

@Data
public class ReviewReq {

    private String reviewText;
    private Double ratings;
    private List<String> productImages;
}
