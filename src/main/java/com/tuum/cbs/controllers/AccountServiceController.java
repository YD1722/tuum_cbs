package com.tuum.cbs.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountServiceController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
