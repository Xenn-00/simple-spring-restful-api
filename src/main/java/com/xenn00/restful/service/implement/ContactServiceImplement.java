package com.xenn00.restful.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.xenn00.restful.entity.Contact;
import com.xenn00.restful.entity.User;
import com.xenn00.restful.model.ContactResponse;
import com.xenn00.restful.model.CreateContactRequest;
import com.xenn00.restful.model.SearchContactRequest;
import com.xenn00.restful.model.UpdateContactRequest;
import com.xenn00.restful.repository.ContactRepository;
import com.xenn00.restful.service.ContactService;
import com.xenn00.restful.service.ValidationService;

import jakarta.persistence.criteria.Predicate;

@Service
public class ContactServiceImplement implements ContactService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ContactRepository contactRepository;

    @Transactional
    @Override
    public ContactResponse create(User user, CreateContactRequest request) {
        validationService.validate(request);
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ContactResponse get(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional
    @Override
    public ContactResponse update(User user, UpdateContactRequest request) {
        validationService.validate(request);
        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();

    }

    @Transactional
    @Override
    public void delete(User user, String id) {
        Contact contact = contactRepository.findFirstByUserAndId(user, id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found");
        });

        contactRepository.delete(contact);

    }

    @Transactional(readOnly = true)
    @Override
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        Specification<Contact> specification = (root, query, buider) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(buider.equal(root.get("user"), user));
            if (Objects.nonNull(request.getName())) {
                predicates.add(buider.or(
                        buider.like(root.get("firstName"), "%" + request.getName() + "%"),
                        buider.like(root.get("lastName"), "%" + request.getName() + "%")));
            }

            if (Objects.nonNull(request.getEmail())) {
                predicates.add(buider.or(
                        buider.like(root.get("email"), "%" + request.getEmail() + "%")));
            }

            if (Objects.nonNull(request.getPhone())) {
                predicates.add(buider.or(
                        buider.like(root.get("phone"), "%" + request.getPhone() + "%")));
            }

            return query.where(predicates.toArray(new Predicate[] {})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream().map(contact -> ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build()).collect(Collectors.toList());

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }

}
