package com.rcr.service;

import com.rcr.model.User;

public interface UserService {
    public User findUserByJwtToken(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
}
