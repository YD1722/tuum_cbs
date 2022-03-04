package com.tuum.cbs.controllers;

import com.tuum.cbs.beans.common.request.AccountRequest;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.helpers.validators.AccountRequestValidator;
import com.tuum.cbs.helpers.validators.RequestValidatorI;
import com.tuum.cbs.services.AccountServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccountController {
    private RequestValidatorI accountRequestValidator;
    private AccountServiceI accountServiceI;

    public AccountController(AccountServiceI accountServiceI) {
        this.accountServiceI = accountServiceI;
    }

    @PostMapping("/createAccount")
    public ResponseEntity createNewAccount(@Valid @RequestBody AccountRequest accountRequest) {
        try {
            // TODO: Replace with custom annotation
            this.accountRequestValidator = new AccountRequestValidator(accountRequest);
            this.accountRequestValidator.validate();

            if (!this.accountRequestValidator.isValid()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(this.accountRequestValidator.getValidationResults());
            }

            Response response = accountServiceI.createNewAccount(accountRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return (ResponseEntity) ResponseEntity.internalServerError();
        }
    }

    @GetMapping("/getAccount")
    public ResponseEntity getAccountDetails(@RequestParam String accountId) {
        try {
            Response response = accountServiceI.getAccountDetails(accountId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return (ResponseEntity) ResponseEntity.internalServerError();
        }
    }
}
