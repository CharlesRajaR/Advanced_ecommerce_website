package com.rcr.controller;

import com.rcr.configuration.JwtTokenProvider;
import com.rcr.customReq.LoginReq;
import com.rcr.customRes.ApiResponse;
import com.rcr.customRes.AuthResponse;
import com.rcr.domain.AccountStatus;
import com.rcr.model.Seller;
import com.rcr.model.VerificationCode;
import com.rcr.repository.VerificationCodeRepository;
import com.rcr.service.AuthService;
import com.rcr.service.EmailService;
import com.rcr.service.SellerService;
import com.rcr.serviceimpl.CustomUserService;
import com.rcr.util.OTPutil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller-api")
public class SellerController {
    private OTPutil otPutil;
    private final CustomUserService customUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SellerService sellerService;
    private final EmailService emailService;
    private final AuthService authService;
    private final VerificationCodeRepository verificationCodeRepository;
    @PostMapping("/create-seller")
    public ResponseEntity<Seller> createSeller(@RequestBody  Seller seller) throws Exception {
        Seller seller1 = sellerService.createSeller(seller);

        String otp = otPutil.generateOtp();
        String subject = "verify your account with this link";
        String text = "welcome to rcr bazaar click the link to verify your account";
        String url = "https://localhost:3000/verify-seller/";
        emailService.sendVerificationOTPEmail(seller1.getEmail(), otp, subject, text + url);

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(seller.getEmail());

        if(verificationCode != null){
            verificationCodeRepository.delete(verificationCode);
        }

        verificationCode.setEmail(seller.getEmail());
        verificationCode.setOtp(otp);
        verificationCode.setSeller(seller1);
        verificationCodeRepository.save(verificationCode);

        return ResponseEntity.ok(seller);
    }

    @PostMapping("/send-otp/seller")
    public ResponseEntity<ApiResponse> sendOtpSeller(@RequestBody String email) throws Exception {
        email = "seller_"+email;
        authService.sendLoginOtp(email);
        ApiResponse response = new ApiResponse();
        response.setMessage("otp sent successfully");

        return ResponseEntity.ok(response);

    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verify(@PathVariable String otp, @RequestBody String email)throws Exception{
        Seller  seller = sellerService.verifyEmail(email, otp);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/login/seller")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginReq req)throws Exception{
        String email = req.getEmail();
        String otp = req.getOtp();

        if(verificationCodeRepository.findByEmail(email) == null || !verificationCodeRepository.findByEmail(email).getOtp().equals(otp)){
            throw new Exception("invalid otp");
        }

        UserDetails userDetails = customUserService.loadUserByUsername("seller_"+req.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwtTokenProvider.generateToken(authentication));
        response.setMessage("login successfully...");
        response.setRole(userDetails.getAuthorities().iterator().next().getAuthority().toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/seller/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {
        Seller seller = sellerService.getSellerById(id);

        return ResponseEntity.ok(seller);
    }

    @GetMapping("/api/seller-profile")
    public ResponseEntity<Seller> getSellerProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        String email = jwtTokenProvider.findEmailByJwtToken(jwt);

        Seller seller = sellerService.getSellerByEmail(email);

        return ResponseEntity.ok(seller);
    }

    @GetMapping("/api/get-all-sellers")
    public ResponseEntity<List<Seller>> getAllSeller(@RequestParam AccountStatus status){
        List<Seller> sellers = sellerService.getAllSellers(status);

        return ResponseEntity.ok(sellers);
    }

    @PatchMapping("/api/update/seller")
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws Exception {
        String email = jwtTokenProvider.findEmailByJwtToken(jwt);
        Seller seller1 = sellerService.updateSeller(sellerService.getSellerByEmail(email).getId(), seller);

        return ResponseEntity.ok(seller1);
    }

    @DeleteMapping("/api/delete/seller")
    public ResponseEntity<String> deleteSeller(@RequestHeader("Authorization") String jwt) throws Exception {
        sellerService.deleteSeller(sellerService
                .getSellerByEmail(jwtTokenProvider.findEmailByJwtToken(jwt)).getId());

        return ResponseEntity.ok("seller deleted successfully");
    }
}
