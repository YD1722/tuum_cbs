package com.tuum.cbs.beans.common.request;

import java.util.List;

public class AccountRequest {
    private int customerId;
    private int countryId;
    private List<String> currencyCodeList;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public List<String> getCurrencyCodeList() {
        return currencyCodeList;
    }

    public void setCurrencyCodeList(List<String> currencyCodeList) {
        this.currencyCodeList = currencyCodeList;
    }
}
