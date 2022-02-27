package com.tuum.cbs.controllers;

import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.request.AccountInput;
import com.tuum.cbs.services.AccountServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getAccount")
    public ResponseEntity getAccountDetails(@RequestParam String accountId) {
        try {
            BankAccount bankAccount = accountServiceI.getAccountDetails(accountId);

            if (bankAccount == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Account not found");
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(bankAccount);
        } catch (Exception e) {
            return (ResponseEntity) ResponseEntity.internalServerError();
        }
    }
}
