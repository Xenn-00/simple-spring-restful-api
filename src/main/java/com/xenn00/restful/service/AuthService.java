package com.xenn00.restful.service;

import org.springframework.stereotype.Service;

import com.xenn00.restful.entity.User;
import com.xenn00.restful.model.LoginUserRequest;
import com.xenn00.restful.model.TokenResponse;

@Service
public interface AuthService {
    TokenResponse login(LoginUserRequest request);

    void logout(User user);
}
