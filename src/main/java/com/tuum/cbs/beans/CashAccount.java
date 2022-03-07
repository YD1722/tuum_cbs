package com.tuum.cbs.beans;

import java.math.BigDecimal;

public class CashAccount {
    // TODO : New bean for view data
    // TODO: handle number of decimal points in a settings file
    private int customerId;
    private int cashAccountId;
    private String accountId;
    private String currencyCode;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal availableBalance = BigDecimal.ZERO;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getCashAccountId() {
        return cashAccountId;
    }

    public void setCashAccountId(int cashAccountId) {
        this.cashAccountId = cashAccountId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }
}
