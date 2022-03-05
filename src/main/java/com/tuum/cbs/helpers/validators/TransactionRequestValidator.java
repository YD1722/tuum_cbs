package com.tuum.cbs.helpers.validators;

import com.tuum.cbs.beans.common.TransactionDirection;
import com.tuum.cbs.beans.common.requests.TransactionCreateRequest;

import java.math.BigDecimal;

public class TransactionRequestValidator extends RequestValidator {
    private TransactionCreateRequest transactionCreateRequest;

    public TransactionRequestValidator(TransactionCreateRequest transactionCreateRequest) {
        this.transactionCreateRequest = transactionCreateRequest;
    }

    @Override
    public void validate() {
        validateCurrency();
        validateAmount();
        validateDirection();
    }

    private void validateCurrency() {
        if (!CommonValidationHelper.isCurrencySupported(this.transactionCreateRequest.getCurrencyCode())) {
            validationResults.add("Invalid currency code");
        }
    }

    private void validateAmount() {
        if (this.transactionCreateRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            validationResults.add("Invalid amount");
        }
    }

    private void validateDirection() {
        if (!isDirectionValid()) {
            validationResults.add("Invalid transaction direction");
        }
    }

    private boolean isDirectionValid() {
        return this.transactionCreateRequest.getDirection() == TransactionDirection.IN.getValue()
                || this.transactionCreateRequest.getDirection() == TransactionDirection.OUT.getValue();
    }
}

