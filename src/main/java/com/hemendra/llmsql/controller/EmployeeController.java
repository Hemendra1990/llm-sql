package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/setup-reference-data")
    public ResponseEntity<String> setupReferenceData() {
        return ResponseEntity.ok(employeeService.setupReferenceData());
    }

    @PostMapping("/generate/{count}")
    public ResponseEntity<String> generateEmployees(@PathVariable Integer count) {
        return ResponseEntity.ok(employeeService.generateAndSaveEmployees(count));
    }
}