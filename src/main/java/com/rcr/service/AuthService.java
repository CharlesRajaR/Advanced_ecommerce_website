package com.rcr.service;

import com.rcr.customReq.LoginReq;
import com.rcr.customReq.SignUpReq;
import com.rcr.customRes.AuthResponse;

public interface AuthService {
    public String createUser(SignUpReq req) throws Exception;

    public void sendLoginOtp(String email) throws Exception;

    public AuthResponse login(LoginReq req) throws Exception;
}
