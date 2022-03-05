package com.tuum.cbs.beans.common.requests;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AccountCreateRequest {
    @Min(1)
    private int customerId;

    @Min(1)
    private int countryId;

    @NotNull
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
