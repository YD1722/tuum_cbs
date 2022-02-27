package com.tuum.cbs.beans;

import java.util.List;

public class BankAccount {
    private String accountId;
    private Integer customerId;
    private List<CashAccount> cashAccountList;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<CashAccount> getCashAccountList() {
        return cashAccountList;
    }

    public void setCashAccountList(List<CashAccount> cashAccountList) {
        this.cashAccountList = cashAccountList;
    }
}
