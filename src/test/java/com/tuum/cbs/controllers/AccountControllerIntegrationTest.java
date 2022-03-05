package com.tuum.cbs.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.cbs.beans.common.request.AccountRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class AccountControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void CreateAccountTests() throws Exception {
        AccountRequest accountRequests = new AccountRequest();

        accountRequests.setCustomerId(1);
        accountRequests.setCountryId(1);
        accountRequests.setCurrencyCodeList(List.of(new String[]{"EUR", "USD"}));

        this.mvc.perform(post("/createAccount")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequests))
                )
                .andExpect(status().isOk())
                .andReturn();
    }
}
