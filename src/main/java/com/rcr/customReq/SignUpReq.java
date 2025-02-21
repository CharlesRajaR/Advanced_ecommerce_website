package com.rcr.customReq;

import com.rcr.domain.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpReq {
    private String email;
    private String  password;
    private UserRole role;
    private String otp;
    private String userName;
    private String phoneNumber;
}
