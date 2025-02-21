package com.rcr.customReq;

import lombok.Data;

@Data
public class CartItemReq {
    private Long productId;
    private int quantity;
    private String size;
}
