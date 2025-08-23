package com.example.employee.management.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payrolls")
public class CPayRoll {

    private String strEmpId;   // reference to employee
    private String yearMonth;  // format "YYYY-MM" (e.g. "2025-08")

    private double basicSalary;
    private double hra;          // House Rent Allowance
    private double da;           // Dearness Allowance
    private double deductions;   // e.g. PF, Tax
    private double netPay;       // computed (basic+hra+da - deductions)
}
