package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lead")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @PostMapping
    public ResponseEntity<String> insertLead(@RequestParam("count") int count) {
        String savedLead = leadService.generateAndSaveLead(count);
        return ResponseEntity.ok(savedLead);
    }
}
