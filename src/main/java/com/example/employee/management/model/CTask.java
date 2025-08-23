package com.example.employee.management.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tasks")
public class CTask {

    @Id
    private String taskId;

    private String projectId;   // link to project
    private String assignedEmpId; // employee assigned
    private String taskTitle;
    private String taskDescription;

    private LocalDate dueDate;

    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;
}
