package com.tuum.cbs.helpers.validators;

import com.tuum.cbs.beans.common.TransactionDirection;
import com.tuum.cbs.beans.common.request.TransactionRequest;

import java.math.BigDecimal;

public class TransactionRequestValidator extends RequestValidator {
    private TransactionRequest transactionRequest;

    public TransactionRequestValidator(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    @Override
    public void validate() {
        validateCurrency();
        validateAmount();
        validateDirection();
    }

    private void validateCurrency() {
        if (!CommonValidationHelper.isCurrencySupported(this.transactionRequest.getCurrencyCode())) {
            validationResults.add("Invalid currency code");
        }
    }

    private void validateAmount() {
        if (this.transactionRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            validationResults.add("Invalid amount");
        }
    }

    private void validateDirection() {
        if (!isDirectionValid()) {
            validationResults.add("Invalid transaction direction");
        }
    }

    private boolean isDirectionValid() {
        return this.transactionRequest.getDirection() == TransactionDirection.IN.getValue()
                || this.transactionRequest.getDirection() == TransactionDirection.OUT.getValue();
    }
}

