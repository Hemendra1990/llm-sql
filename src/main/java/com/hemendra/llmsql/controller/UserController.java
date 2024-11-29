package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/generate/{count}")
    public ResponseEntity<String> insertUser(@PathVariable Integer count) {
        String summary = userService.generateAndSaveUsers(count);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/setup-reference-data")
    public ResponseEntity<String> setupReferenceData() {
        String summary = userService.setupReferenceData();
        return ResponseEntity.ok(summary);
    }
}
