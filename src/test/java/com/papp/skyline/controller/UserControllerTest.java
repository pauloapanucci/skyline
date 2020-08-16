package com.papp.skyline.controller;

import com.papp.skyline.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest extends SkylineTestController {
    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldCreateNewUser() throws Exception {
        MvcResult result = testPost(API_USERS, USER_JSON, status().isCreated());
        Assert.assertEquals(USER_JSON, result.getResponse().getContentAsString());
    }

    @Test
    public void shouldNotCreateUserDueToSameCpf() throws Exception {
        testPost(API_USERS, USER_JSON, status().isCreated());
        testPost(API_USERS, USER_JSON, status().isOk());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassNameToRequest() throws Exception {
        testPost(API_USERS, USER_JSON_WITHOUT_NAME, status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassCpfToRequest() throws Exception {
        testPost(API_USERS, USER_JSON_WITHOUT_CPF, status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenNotPassNameAndCpfToRequest() throws Exception {
        mockMvc.perform(post(API_USERS))
                .andExpect(status().isBadRequest());
    }

}
