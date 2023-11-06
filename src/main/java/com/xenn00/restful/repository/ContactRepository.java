package com.xenn00.restful.repository;

import java.util.Optional;

import com.xenn00.restful.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.xenn00.restful.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {
    Optional<Contact> findFirstByUserAndId(User user, String id);

}
