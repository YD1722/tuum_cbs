package com.tuum.cbs.helpers.validators;

import com.tuum.cbs.beans.common.requests.AccountCreateRequest;

import java.util.List;

public class AccountRequestValidator extends RequestValidator {
    private AccountCreateRequest accountCreateRequest;

    public AccountRequestValidator(AccountCreateRequest accountCreateRequest){
        this.accountCreateRequest = accountCreateRequest;
    }

    @Override
    public void validate() {
        validateCurrency();
    }

    private void validateCurrency() {
        List<String> currencyList = this.accountCreateRequest.getCurrencyCodeList();

        for (String code : currencyList) {
            if (!CommonValidationHelper.isCurrencySupported(code)) {
                this.validationResults.add("Invalid currency code : " + code);
                return;
            }
        }
    }
}
