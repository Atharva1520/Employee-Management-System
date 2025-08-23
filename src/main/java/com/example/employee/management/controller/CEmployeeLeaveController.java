package com.example.employee.management.controller;

import com.example.employee.management.dto.ApiResponse;
import com.example.employee.management.model.CEmployeeLeave;
import com.example.employee.management.model.LeaveType;
import com.example.employee.management.service.CEmployeeLeaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/leave")
public class CEmployeeLeaveController {

    private final CEmployeeLeaveService leaveService;

    public CEmployeeLeaveController(CEmployeeLeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // Apply for a leave
    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<?>> applyLeave(@RequestBody CEmployeeLeave leaveRequest, Principal principal) {
        leaveRequest.setStrEmpId(principal.getName());
        String message = leaveService.applyLeave(leaveRequest);

        if (message.contains("Insufficient")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, false, message, null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, true, message, leaveRequest));
    }


    // Fetch all applied leaves by employee
    @GetMapping("/my-leaves")
    public ResponseEntity<ApiResponse<?>> myLeaves(Principal principal) {
        String empId = principal.getName();
        List<CEmployeeLeave> leaves = leaveService.getLeaves(empId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, true, "Leaves fetched", leaves)
        );
    }

    @PutMapping("/approve/{leaveId}")
    public ResponseEntity<ApiResponse<?>> approveLeave(
            @PathVariable String leaveId,
            @RequestParam boolean approve,
            Principal principal) {

        // principal.getName() gives logged-in manager's empId
        String managerEmpId = principal.getName();
        String message = leaveService.approveLeave(leaveId, managerEmpId, approve);

        if (message.contains("Access denied")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, false, message, null));
        }
        if (message.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, false, message, null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, true, message, null));
    }

}
