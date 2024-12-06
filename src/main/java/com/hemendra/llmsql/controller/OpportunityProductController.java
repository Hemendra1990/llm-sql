package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.OpportunityProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/opportunity-product")
public class OpportunityProductController {

    private final OpportunityProductService opportunityProductService;

    public OpportunityProductController(OpportunityProductService opportunityProductService) {
        this.opportunityProductService = opportunityProductService;
    }

    @PostMapping
    public String createOpportunityProduct(@RequestParam int count) {
        opportunityProductService.generateAndSaveOpportunityProduct(count);
        return "OpportunityProduct created " + count + " records.";
    }
}