package com.example.employee.management.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employees")
public class CEmployeeModel {
    private String strName;
    private String strMail;

    @Indexed(unique = true)
    private String strEmpId;

    private String strDateOfBirth;
    private String strPassword;
    private String strRole;

}
