package com.tuum.cbs.controllers;

import com.tuum.cbs.beans.common.request.AccountRequest;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.services.AccountServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tuum.cbs.helpers.AccountHelper.isRequestValid;

@RestController
public class AccountController {
    private AccountServiceI accountServiceI;

    public AccountController(AccountServiceI accountServiceI) {
        this.accountServiceI = accountServiceI;
    }

    @PostMapping("/createAccount")
    public ResponseEntity createNewAccount(@RequestBody AccountRequest accountRequest) {
        try {
            if (!isRequestValid(accountRequest)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Invalid currency");
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
