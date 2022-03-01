package com.tuum.cbs.controllers;

import com.tuum.cbs.beans.common.request.TransactionRequest;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.helpers.RequestValidatorI;
import com.tuum.cbs.helpers.TransactionRequestValidator;
import com.tuum.cbs.services.TransactionServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    RequestValidatorI transactionRequestValidator; // TODO: Check here
    TransactionServiceI transactionServiceI;

    public TransactionController(TransactionServiceI transactionServiceI) {
        this.transactionServiceI = transactionServiceI;
    }

    @PostMapping("/transaction")
    public ResponseEntity transaction(@RequestBody TransactionRequest transactionRequest) {

        try {
            this.transactionRequestValidator = new TransactionRequestValidator(transactionRequest); // TODO: Constructor DI
            this.transactionRequestValidator.validate();

            if (!this.transactionRequestValidator.isValid()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(this.transactionRequestValidator.getValidationResults()); // TODO: Revisit this
            }

            Response response = this.transactionServiceI.handleTransaction(transactionRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return (ResponseEntity) ResponseEntity.internalServerError();
        }
    }
}
