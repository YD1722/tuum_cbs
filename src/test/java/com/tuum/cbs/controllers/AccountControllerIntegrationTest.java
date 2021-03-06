package com.tuum.cbs.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.cbs.beans.common.ResponseStatus;
import com.tuum.cbs.beans.common.requests.AccountCreateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void createNewAccountShouldSuccess() throws Exception {
        AccountCreateRequest accountCreateRequests = new AccountCreateRequest();

        accountCreateRequests.setCustomerId(1);
        accountCreateRequests.setCountryId(1);
        accountCreateRequests.setCurrencyCodeList(List.of(new String[]{"EUR", "USD"}));

        this.mvc.perform(post("/createAccount")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateRequests))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.accountId").exists());
    }

    @Test
    public void shouldNotCreateNewCashAccountsIfAlreadyExists() throws Exception {
        AccountCreateRequest accountCreateRequest_1 = new AccountCreateRequest();

        accountCreateRequest_1.setCustomerId(2);
        accountCreateRequest_1.setCountryId(1);
        accountCreateRequest_1.setCurrencyCodeList(List.of(new String[]{"EUR", "USD"}));

        this.mvc.perform(post("/createAccount")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateRequest_1))
                )
                .andExpect(status().isOk());

        AccountCreateRequest accountCreateRequest_2 = new AccountCreateRequest();

        accountCreateRequest_2.setCustomerId(3);
        accountCreateRequest_2.setCountryId(1);
        accountCreateRequest_2.setCurrencyCodeList(List.of(new String[]{"EUR", "USD"}));

        this.mvc.perform(post("/createAccount")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateRequest_1))
                )
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(ResponseStatus.ERROR.toString()));
    }

    @Test
    public void getAccountShouldSuccess() throws Exception {
        mvc.perform(get("/getAccount?accountId=TEST_1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.customerId").value(100));
    }
}
