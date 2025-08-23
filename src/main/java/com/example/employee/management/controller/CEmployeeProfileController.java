package com.example.employee.management.controller;

import com.example.employee.management.dto.ApiResponse;
import com.example.employee.management.dto.PasswordChangeRequest;
import com.example.employee.management.model.CEmployeeProfile;
import com.example.employee.management.security.JwtUtil;
import com.example.employee.management.service.CEmployeeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/profile")
public class CEmployeeProfileController {

    private final CEmployeeProfileService profileService;

    public CEmployeeProfileController(CEmployeeProfileService profileService) {
        this.profileService = profileService;
    }

    // View profile
    @GetMapping
    public ResponseEntity<ApiResponse<?>> viewProfile(Principal principal) {
        String empId = principal.getName(); // empId comes from basic auth
        Optional<CEmployeeProfile> profile = profileService.viewProfile(empId);

        if (profile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), false,
                            "Profile not found", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "Profile fetched", profile.get())
        );
    }

    // Update profile
    @PutMapping
    public ResponseEntity<ApiResponse<?>> updateProfile(Principal principal,
                                                        @RequestBody CEmployeeProfile updatedProfile) {
        String empId = principal.getName();
        CEmployeeProfile profile = profileService.updateProfile(empId, updatedProfile);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "Profile updated", profile)
        );
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(
            @RequestBody PasswordChangeRequest request) {

        // empId comes from request body now
        boolean success = profileService.changePassword(request.getEmpId(),
                request.getOldPassword(),
                request.getNewPassword());

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), false,
                            "Old password is incorrect or empId not found", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, "Password changed successfully", null)
        );
    }

}
