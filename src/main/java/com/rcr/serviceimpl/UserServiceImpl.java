package com.rcr.serviceimpl;

import com.rcr.configuration.JwtTokenProvider;
import com.rcr.model.User;
import com.rcr.repository.UserRepository;
import com.rcr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtTokenProvider.findEmailByJwtToken(jwt);
        User user = findUserByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        return userRepository.findByEmail(email);
    }
}
