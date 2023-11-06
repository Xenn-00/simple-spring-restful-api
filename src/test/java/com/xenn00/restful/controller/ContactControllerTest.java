package com.xenn00.restful.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenn00.restful.entity.Contact;
import com.xenn00.restful.entity.User;
import com.xenn00.restful.model.ContactResponse;
import com.xenn00.restful.model.CreateContactRequest;
import com.xenn00.restful.model.UpdateContactRequest;
import com.xenn00.restful.model.WebResponse;
import com.xenn00.restful.repository.ContactRepository;
import com.xenn00.restful.repository.UserRpository;
import com.xenn00.restful.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRpository userRpository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRpository.deleteAll();

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("Test");
        user.setPassword(BCrypt.hashpw("test123", BCrypt.gensalt()));
        user.setName("test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000);

        userRpository.save(user);
    }

    @Test
    void testCreateContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("falseEmail");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testCreateContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("new");
        request.setLastName("test");
        request.setEmail("test@test.com");
        request.setPhone("+6285881342359");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getData());
                    assertEquals("new", response.getData().getFirstName());
                    assertTrue(contactRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void testGetContactNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/21312313213")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testGetContactSuccess() throws Exception {
        User user = userRpository.findFirstByUsername("Test").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("new");
        contact.setLastName("test");
        contact.setEmail("test@test.com");
        contact.setPhone("+6285881342359");
        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getData());
                    assertEquals(contact.getId(), response.getData().getId());
                });
    }

    @Test
    void testUpdateContactBadRequest() throws Exception {
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("");
        request.setEmail("falseEmail");

        mockMvc.perform(
                put("/api/contacts/131321")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isBadRequest())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testUpdateContactSuccess() throws Exception {
        User user = userRpository.findFirstByUsername("Test").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("before");
        contact.setLastName("test");
        contact.setEmail("test@test.com");
        contact.setPhone("+6285881342359");
        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("after");
        request.setLastName("test");
        request.setEmail("test@test.com");
        request.setPhone("+6285881342359");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertNotNull(response.getData());
                    assertEquals(request.getFirstName(), response.getData().getFirstName());
                });
    }

    @Test
    void testDeleteContactNotFound() throws Exception {
        mockMvc.perform(
                delete("/api/contacts/21312313213")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isNotFound())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void testDeleteContactSuccess() throws Exception {
        User user = userRpository.findFirstByUsername("Test").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("new");
        contact.setLastName("test");
        contact.setEmail("test@test.com");
        contact.setPhone("+6285881342359");
        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals("OK", response.getData());
                });
    }

    @Test
    void testSearchContactNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(0, response.getData().size());
                    assertEquals(0, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });
    }

    @Test
    void testSearchContactSuccess() throws Exception {
        User user = userRpository.findFirstByUsername("Test").orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setUser(user);
            contact.setId(UUID.randomUUID().toString());
            contact.setFirstName("before" + i);
            contact.setLastName("test");
            contact.setEmail("test@test.com");
            contact.setPhone("+6285881342359");
            contactRepository.save(contact);
        }

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "before")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("email", "test.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "+62858")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "+62858")
                        .queryParam("page", "1000")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test"))
                .andExpectAll(
                        status().isOk())
                .andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNull(response.getErrors());
                    assertEquals(0, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(1000, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });
    }

}
