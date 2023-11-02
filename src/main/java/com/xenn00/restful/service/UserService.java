package com.xenn00.restful.service;

import org.springframework.stereotype.Service;

import com.xenn00.restful.entity.User;
import com.xenn00.restful.model.RegisterUserRequest;
import com.xenn00.restful.model.UpdateUserRequest;
import com.xenn00.restful.model.UserResponse;

@Service
public interface UserService {
    public void register(RegisterUserRequest request);

    UserResponse get(User user);

    UserResponse update(User user, UpdateUserRequest request);

}
