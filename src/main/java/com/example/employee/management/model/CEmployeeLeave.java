package com.example.employee.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employee_leave")
public class CEmployeeLeave {

    @Id
    private String id;

    private String strEmpId;

    private LeaveType leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private int daysApplied;

    private String status;

    private int remainingSickLeave;

    private int remainingCasualLeave;
}

