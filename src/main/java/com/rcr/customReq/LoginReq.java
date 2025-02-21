package com.rcr.customReq;

import lombok.Data;

@Data
public class LoginReq {
    private String email;
    private String otp;
}
