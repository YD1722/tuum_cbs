package com.tuum.cbs.helpers.validators;

import com.tuum.cbs.beans.common.request.AccountRequest;

import java.util.List;

public class AccountRequestValidator extends RequestValidator {
    private AccountRequest accountRequest;

    public AccountRequestValidator(AccountRequest accountRequest){
        this.accountRequest = accountRequest;
    }

    @Override
    public void validate() {
        validateCurrency();
    }

    private void validateCurrency() {
        List<String> currencyList = this.accountRequest.getCurrencyCodeList();

        for (String code : currencyList) {
            if (!CommonValidationHelper.isCurrencySupported(code)) {
                this.validationResults.add("Invalid currency code : " + code);
                return;
            }
        }
    }
}
