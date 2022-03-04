package com.tuum.cbs.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountMapperIntegrationTest {

    @Autowired
    AccountMapper accountMapper;

    @Test
    public void whenRecordsInDatabase_shouldReturnArticleWithGivenId() {
    }
}
