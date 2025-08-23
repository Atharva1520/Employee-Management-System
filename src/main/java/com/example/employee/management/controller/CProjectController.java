package com.example.employee.management.controller;

import com.example.employee.management.dto.ApiResponse;
import com.example.employee.management.model.CProject;
import com.example.employee.management.model.CTask;
import com.example.employee.management.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class CProjectController {

    private final ProjectService projectService;

    public CProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Manager creates project
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createProject(@RequestBody CProject project, Principal principal) {
        project.setManagerEmpId(principal.getName());
        CProject saved = projectService.createProject(project);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Project created", saved));
    }

    // Manager adds task
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<ApiResponse<?>> addTask(@PathVariable String projectId, @RequestBody CTask task) {
        CTask savedTask = projectService.addTaskToProject(projectId, task);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Task added to project", savedTask));
    }

    // Get all tasks for a project
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<ApiResponse<?>> getTasks(@PathVariable String projectId) {
        List<CTask> tasks = projectService.getTasksForProject(projectId);
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Tasks fetched", tasks));
    }

    // Get projects for logged-in manager
    @GetMapping("/my-projects")
    public ResponseEntity<ApiResponse<?>> getMyProjects(Principal principal) {
        List<CProject> projects = projectService.getProjectsByManager(principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Projects fetched", projects));
    }

    // Employee: view their tasks
    @GetMapping("/my-tasks")
    public ResponseEntity<ApiResponse<?>> getMyTasks(Principal principal) {
        List<CTask> tasks = projectService.getTasksForEmployee(principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(200, true, "My tasks fetched", tasks));
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<ApiResponse<?>> completeTask(@PathVariable String taskId, Principal principal) {
        CTask updatedTask = projectService.markTaskAsCompleted(taskId, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(200, true, "Task marked as completed", updatedTask));
    }
}
