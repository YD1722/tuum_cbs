package com.tuum.cbs.mapper;

import com.tuum.cbs.beans.BankAccount;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountMapperIntegrationTest {

    @Autowired
    AccountMapper accountMapper;

    @Test
    public void whenRecordsInDatabase_shouldReturnArticleWithGivenId() {
        BankAccount account = accountMapper.getAccountByCustomerId(1);

        Assert.assertNotNull(account);
    }
}
