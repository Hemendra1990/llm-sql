package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public String createAccount(@RequestParam int count) {
        accountService.generateAndSaveAccount(count);
        return "Account created "+ count + " records.";
    }
}
