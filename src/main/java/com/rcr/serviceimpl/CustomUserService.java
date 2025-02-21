package com.rcr.serviceimpl;

import com.rcr.model.Seller;
import com.rcr.model.User;
import com.rcr.repository.SellerRepository;
import com.rcr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String SELLER_PREFIX = "seller_";
        if(username.startsWith(SELLER_PREFIX)){
            Seller seller = sellerRepository.findByEmail(username.substring(SELLER_PREFIX.length()));
            if(seller == null){
                throw new UsernameNotFoundException("seller not found");
            }
            String password = seller.getPassword();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(seller.getRole().toString()));
            return new org.springframework.security.core.userdetails.User(username.substring(SELLER_PREFIX.length()), password, authorities);
        }


        User user = userRepository.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("user not found with this email...");
        }

        String password = user.getPassword();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(username, password, authorities);
    }
}
