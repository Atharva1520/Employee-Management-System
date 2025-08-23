package com.example.employee.management.controller;

import com.example.employee.management.dto.ApiResponse;
import com.example.employee.management.model.CPayRoll;
import com.example.employee.management.service.PayrollService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class CPayrollController {

    private final PayrollService payrollService;

    public CPayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/create/{yearMonth}")
    public ResponseEntity<ApiResponse<?>> createPayroll(
            @PathVariable String yearMonth,
            @RequestBody CPayRoll payroll,
            Principal principal) {

        String managerEmpId = principal.getName(); // logged-in manager
        String message = payrollService.createPayroll(payroll, managerEmpId, yearMonth);

        if (message.contains("Access denied")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, false, message, null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, true, message, payroll));
    }
    // Get salary breakdown (all months)
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<?>> getPayrollHistory(Principal principal) {
        String empId = principal.getName(); // logged-in employee
        List<CPayRoll> payrolls = payrollService.getPayrollHistory(empId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, true, "Payroll history fetched", payrolls)
        );
    }

    // Get salary breakdown for a specific month
    @GetMapping("/{yearMonth}")
    public ResponseEntity<ApiResponse<?>> getPayrollForMonth(
            @PathVariable String yearMonth,
            Principal principal) {
        String empId = principal.getName();
        CPayRoll payroll = payrollService.getPayrollForMonth(empId, yearMonth);
        return ResponseEntity.ok(
                new ApiResponse<>(200, true, "Payroll fetched", payroll)
        );
    }

    @GetMapping("/download/{yearMonth}")
    public ResponseEntity<byte[]> downloadPayslip(
            @PathVariable String yearMonth,
            Principal principal) {

        try {
            String empId = principal.getName();
            ByteArrayInputStream bis = payrollService.downloadPayslip(empId, yearMonth);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=payslip-" + yearMonth + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(bis.readAllBytes());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
