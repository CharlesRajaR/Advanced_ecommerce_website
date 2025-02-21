package com.rcr.serviceimpl;

import com.rcr.configuration.JwtTokenProvider;
import com.rcr.customReq.LoginReq;
import com.rcr.customReq.SignUpReq;
import com.rcr.customRes.AuthResponse;
import com.rcr.domain.UserRole;
import com.rcr.model.Cart;
import com.rcr.model.Seller;
import com.rcr.model.User;
import com.rcr.model.VerificationCode;
import com.rcr.repository.CartRepository;
import com.rcr.repository.SellerRepository;
import com.rcr.repository.UserRepository;
import com.rcr.repository.VerificationCodeRepository;
import com.rcr.service.AuthService;
import com.rcr.service.EmailService;
import com.rcr.util.OTPutil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserService customUserService;
    private final SellerRepository sellerRepository;
    @Override
    public String createUser(SignUpReq req) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())){
            throw new Exception("wrong otp...");
        }

        User user = userRepository.findByEmail(req.getEmail());
        if(user != null){
            throw new Exception("user already found with this email id...");
        }

        User newUser = new User();
        newUser.setEmail(req.getEmail());
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setRole(req.getRole());
        newUser.setUsername(req.getUserName());
        newUser.setPhoneNumber(req.getPhoneNumber());

        User save = userRepository.save(newUser);

        Cart cart = new Cart();
        cart.setUser(save);

        cartRepository.save(cart);

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(req.getRole().toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        return jwt;
    }

    @Override
    public void sendLoginOtp(String email) throws Exception {
        String SINGNING_PREFIX = "signin_";
        String SELLER_PREFIX = "seller_";

        if(email.startsWith(SINGNING_PREFIX)){
            email = email.substring(SINGNING_PREFIX.length());
            User user = userRepository.findByEmail(email);
            if(user == null){
                throw new Exception("user not found with this email");
            }
        }

        if(email.startsWith(SELLER_PREFIX)){
            email = email.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(email);
            if(seller == null){
                throw new Exception("seller not found with this email");
            }
        }


        String otp = OTPutil.generateOtp();

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if(verificationCode != null){
            verificationCodeRepository.delete(verificationCode);
        }

        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(otp);
        verificationCode1.setEmail(email);

        verificationCodeRepository.save(verificationCode1);

        String subject = "raja bazaar login or signup otp";
        String text = ":::your one time password:::"+" "+otp;
        emailService.sendVerificationOTPEmail(email, otp, subject, text);
    }

    @Override
    public AuthResponse login(LoginReq req) throws Exception {
        String otp = req.getOtp();
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

        if(verificationCode == null ||  !verificationCode.getOtp().equals(otp)){
            throw new Exception("wrong otp...");
        }

        UserDetails userDetails = customUserService.loadUserByUsername(req.getEmail());
        Authentication authentication = authenticate(userDetails);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setRole(authorities.iterator().next().getAuthority());
        response.setMessage("login successfull");

        return response;
    }

    private Authentication authenticate(UserDetails userDetails)throws Exception{
        if(userDetails == null){
            throw new Exception("user not found...");
        }

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

    }
}
