package com.tuum.cbs.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.cbs.beans.common.TransactionDirection;
import com.tuum.cbs.beans.common.requests.TransactionCreateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class TransactionControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldDoInTransactionSuccessfully() throws Exception {
        TransactionCreateRequest transactionCreateRequest = new TransactionCreateRequest();

        transactionCreateRequest.setAccountId("TEST_1");
        transactionCreateRequest.setAmount(BigDecimal.valueOf(100));
        transactionCreateRequest.setCurrencyCode("EUR");
        transactionCreateRequest.setDirection(TransactionDirection.IN.getValue());
        transactionCreateRequest.setDescription("Integration Test");

        mvc.perform(post("/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionCreateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.newBalance").value(1100));
    }

    @Test
    public void shouldDoOutTransactionSuccessfully() throws Exception {
        TransactionCreateRequest transactionCreateRequest = new TransactionCreateRequest();

        transactionCreateRequest.setAccountId("TEST_2");
        transactionCreateRequest.setAmount(BigDecimal.valueOf(100));
        transactionCreateRequest.setCurrencyCode("EUR");
        transactionCreateRequest.setDirection(TransactionDirection.OUT.getValue());
        transactionCreateRequest.setDescription("Integration Test");

        mvc.perform(post("/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionCreateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.amount").value(100))
                .andExpect(jsonPath("$.data.transactionId").exists());
    }

    @Test
    public void shouldGetTransactionsLogsSuccessfully() throws Exception {
        TransactionCreateRequest transactionCreateRequest = new TransactionCreateRequest();

        transactionCreateRequest.setAccountId("TEST_2");
        transactionCreateRequest.setAmount(BigDecimal.valueOf(100));
        transactionCreateRequest.setCurrencyCode("EUR");
        transactionCreateRequest.setDirection(TransactionDirection.OUT.getValue());
        transactionCreateRequest.setDescription("Integration Test");

        mvc.perform(post("/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionCreateRequest))
                )
                .andExpect(status().isOk());

        mvc.perform(get("/getTransaction?accountId=TEST_2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0]").exists())
                .andExpect(jsonPath("$.data[0].amount").value(100));
    }

}
