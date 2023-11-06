package com.xenn00.restful.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.xenn00.restful.entity.User;
import com.xenn00.restful.model.ContactResponse;
import com.xenn00.restful.model.CreateContactRequest;
import com.xenn00.restful.model.SearchContactRequest;
import com.xenn00.restful.model.UpdateContactRequest;

@Service
public interface ContactService {
    ContactResponse create(User user, CreateContactRequest request);

    ContactResponse get(User user, String id);

    ContactResponse update(User user, UpdateContactRequest request);

    void delete(User user, String id);

    Page<ContactResponse> search(User user, SearchContactRequest request);
}
