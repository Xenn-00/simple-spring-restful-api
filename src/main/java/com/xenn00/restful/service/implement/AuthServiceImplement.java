package com.xenn00.restful.service.implement;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.xenn00.restful.entity.User;
import com.xenn00.restful.model.LoginUserRequest;
import com.xenn00.restful.model.TokenResponse;
import com.xenn00.restful.repository.UserRpository;
import com.xenn00.restful.security.BCrypt;
import com.xenn00.restful.service.AuthService;
import com.xenn00.restful.service.ValidationService;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImplement implements AuthService {

    @Autowired
    private UserRpository userRpository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    @Override
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRpository.findFirstByUsername(request.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong!"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 60 * 24 * 30));
            userRpository.save(user);
            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong!");
        }
    }

    @Transactional
    @Override
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRpository.save(user);
    }

}
