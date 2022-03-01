package com.tuum.cbs.helpers;

import com.tuum.cbs.beans.common.request.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TransactionRequestValidator implements RequestValidatorI {
    private TransactionRequest transactionInput;
    private List<String> validationResults = new ArrayList<>();

    @Autowired
    public TransactionRequestValidator(TransactionRequest transactionInput) {
        this.transactionInput = transactionInput;
    }

    @Override
    public void validate() {
        validateCurrency();
        validateAmount();
        validateAccountDetails();
        validateDescription();
    }

    @Override
    public boolean isValid() {
        return validationResults.size() == 0;
    }

    private void validateCurrency() {
        if (!CommonValidationHelper.isCurrencySupported(this.transactionInput.getCurrencyCode())) {
            validationResults.add("Invalid currency code");
        }
    }

    private void validateAmount() {
        if (this.transactionInput.getAmount() < 0) {
            validationResults.add("Invalid amount");
        }
    }

    private void validateAccountDetails() {
        if (this.transactionInput.getAccountId() == null || this.transactionInput.getAccountId().isEmpty()) {
            validationResults.add("No Account id provided");
        }
    }

    private void validateDescription() {
        if (this.transactionInput.getDescription() == null || this.transactionInput.getDescription().isEmpty()) {
            validationResults.add("No description id provided");
        }
    }

    public List<String> getValidationResults() {
        return validationResults;
    }
}

