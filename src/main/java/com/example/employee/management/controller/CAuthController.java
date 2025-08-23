package com.example.employee.management.controller;

import com.example.employee.management.dto.ApiResponse;
import com.example.employee.management.model.CEmployeeModel;
import com.example.employee.management.service.CAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class CAuthController {

    private final CAuthService authService;

    public CAuthController(CAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUp(@RequestBody CEmployeeModel employee) {
        String savedEmployee = authService.signUp(employee);

        if (savedEmployee == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), false,
                            "Employee already present in the DB", null));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), true,
                        "SignUp successful", savedEmployee));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody Map<String, String> loginRequest) {
        String empId = loginRequest.get("empId");
        String dob = loginRequest.get("dob");
        String password = loginRequest.get("password");

        String token = authService.login(empId, dob, password);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), false,
                            "Invalid Employee ID, Date of Birth, or Password", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "Login successful", token)
        );
    }

}
