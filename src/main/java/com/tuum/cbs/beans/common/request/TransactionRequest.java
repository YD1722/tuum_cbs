package com.tuum.cbs.beans.common.request;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class TransactionRequest {
    @NotEmpty
    private String accountId;

    private BigDecimal amount;

    @NotEmpty
    private String currencyCode; // TODO: Currency ID

    private int direction;

    @NotEmpty
    private String description;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


