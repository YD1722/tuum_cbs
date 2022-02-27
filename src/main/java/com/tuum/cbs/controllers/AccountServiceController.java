package com.tuum.cbs.controllers;

import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.request.AccountInput;
import com.tuum.cbs.services.AccountServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.tuum.cbs.helpers.AccountHelper.isRequestValid;

@RestController
public class AccountServiceController {
    private AccountServiceI accountServiceI;

    public AccountServiceController(AccountServiceI accountServiceI) {
        this.accountServiceI = accountServiceI;
    }

    @PostMapping("/createAccount")
    public ResponseEntity createNewAccount(@RequestBody AccountInput accountInput) {
        try {
            if (!isRequestValid(accountInput)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Invalid currency");
            }

            BankAccount bankAccount = accountServiceI.createNewAccount(accountInput);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bankAccount);
        } catch (Exception e) {
            return (ResponseEntity) ResponseEntity.internalServerError();
        }
    }
}
