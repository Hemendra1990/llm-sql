package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.AccountService;
import com.hemendra.llmsql.service.PricebookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pricebook")
public class PricebookController {


    private final PricebookService pricebookService;

    public PricebookController(PricebookService pricebookService) {
        this.pricebookService = pricebookService;
    }

    @PostMapping
    public String createAccount(@RequestParam int count) {
        pricebookService.generateAndSaveAccount(count);
        return "Account created "+ count + " records.";
    }
}
