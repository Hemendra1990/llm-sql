package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.OpportunityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/opportunity")
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @PostMapping
    public String createAccount(@RequestParam int count) {
        opportunityService.generateAndSaveOpportunity(count);
        return "Opportunity created "+ count + " records.";
    }
}
