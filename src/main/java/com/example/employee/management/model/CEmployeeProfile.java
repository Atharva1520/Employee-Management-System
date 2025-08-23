package com.example.employee.management.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employee_profiles")
public class CEmployeeProfile {

    private String strEmpId;
    private String address;
    private String phoneNumber;
    private String department;
    private String designation;
}
