package com.xenn00.restful.service;

import org.springframework.stereotype.Service;

@Service
public interface ValidationService {
    void validate(Object request);
}
