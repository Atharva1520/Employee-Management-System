package com.example.employee.management.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "projects")
public class CProject {

    @Id
    private String projectId;

    private String projectName;
    private String managerEmpId;  // Reference to manager

    private LocalDate deadline;

    @Builder.Default
    private List<String> taskIds = new ArrayList<>();
}
