package com.example.employee.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CHealthCheck {

    @GetMapping("/api/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "OK", "message", "Server is on.");
    }

}
