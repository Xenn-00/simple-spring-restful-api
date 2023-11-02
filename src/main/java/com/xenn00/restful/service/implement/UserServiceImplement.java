package com.xenn00.restful.service.implement;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import com.xenn00.restful.entity.User;
import com.xenn00.restful.model.RegisterUserRequest;
import com.xenn00.restful.model.UpdateUserRequest;
import com.xenn00.restful.model.UserResponse;
import com.xenn00.restful.repository.UserRpository;
import com.xenn00.restful.security.BCrypt;
import com.xenn00.restful.service.UserService;
import com.xenn00.restful.service.ValidationService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRpository userRpository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    @Override
    public void register(RegisterUserRequest request) {
        validationService.validate(request);
        if (userRpository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered!");
        }

        User user = new User();
        UUID uuid = UUID.randomUUID();
        user.setId(uuid.toString());
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRpository.save(user);
    }

    @Override
    public UserResponse get(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Transactional
    @Override
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }
        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRpository.save(user);
        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

}
