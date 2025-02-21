package com.rcr.controller;

import com.rcr.customReq.LoginReq;
import com.rcr.customReq.SignUpReq;
import com.rcr.customRes.ApiResponse;
import com.rcr.customRes.AuthResponse;
import com.rcr.model.VerificationCode;
import com.rcr.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody  SignUpReq req)throws Exception{
        String jwt = authService.createUser(req);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setRole(req.getRole().toString());
        response.setMessage("registration successful...");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOtp(@RequestBody VerificationCode req)throws Exception{
        authService.sendLoginOtp(req.getEmail());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("otp sent successfully");

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginReq req)throws Exception{

        AuthResponse response = authService.login(req);



        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
