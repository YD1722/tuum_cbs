package com.tuum.cbs.beans.common.response;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountResponse {
    private String accountId;
    private int customerId;
    private Map<String, Object> balances = new HashMap<>();

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void addBalance(String currency, BigDecimal amount) {
        balances.put(currency, amount);
    }
}
