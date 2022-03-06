package com.tuum.cbs.controllers;

import com.tuum.cbs.beans.common.requests.TransactionCreateRequest;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.helpers.validators.RequestValidatorI;
import com.tuum.cbs.helpers.validators.TransactionRequestValidator;
import com.tuum.cbs.services.RabbitMqMessageService;
import com.tuum.cbs.services.TransactionServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@RestController
public class TransactionController {
    RequestValidatorI transactionRequestValidator;
    TransactionServiceI transactionServiceI;
    RabbitMqMessageService rabbitMqMessageService;

    public TransactionController(TransactionServiceI transactionServiceI) {
        this.transactionServiceI = transactionServiceI;
    }

    @Autowired
    public void setRabbitMqMessageService(RabbitMqMessageService rabbitMqMessageService) {
        this.rabbitMqMessageService = rabbitMqMessageService;
    }

    @PostMapping("/transaction")
    public ResponseEntity transaction(@RequestBody TransactionCreateRequest transactionCreateRequest) {
        try {
            this.transactionRequestValidator = new TransactionRequestValidator(transactionCreateRequest);
            this.transactionRequestValidator.validate();

            if (!this.transactionRequestValidator.isValid()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(this.transactionRequestValidator.getValidationResults());
            }

            Response response = this.transactionServiceI.handleTransaction(transactionCreateRequest);

            this.rabbitMqMessageService.send(response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getTransaction")
    public ResponseEntity getTransaction(@RequestParam @NotEmpty String accountId) {
        Response response = this.transactionServiceI.getTransactions(accountId);
        return ResponseEntity.ok(response);
    }
}
