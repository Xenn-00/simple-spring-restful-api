package com.xenn00.restful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xenn00.restful.entity.User;

@Repository
public interface UserRpository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findFirstByUsername(String username);

    Optional<User> findFirstByToken(String token);
}
