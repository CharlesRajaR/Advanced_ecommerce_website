package com.rcr.util;

import lombok.Data;

@Data
public class CalculateDiscount {
    public double findDiscount(int sp, int mp){
        return ((mp - sp)/mp) * 100;
    }
}
