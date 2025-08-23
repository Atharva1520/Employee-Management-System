package com.example.employee.management.service;

import com.example.employee.management.Repository.IProjectRepository;
import com.example.employee.management.Repository.ITaskRepository;
import com.example.employee.management.model.CProject;
import com.example.employee.management.model.CTask;
import com.example.employee.management.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final IProjectRepository projectRepo;
    private final ITaskRepository taskRepo;

    public ProjectService(IProjectRepository projectRepo, ITaskRepository taskRepo) {
        this.projectRepo = projectRepo;
        this.taskRepo = taskRepo;
    }

    // Manager creates a project
    public CProject createProject(CProject project) {
        return projectRepo.save(project);
    }

    // Manager adds a task to project
    public CTask addTaskToProject(String projectId, CTask task) {
        task.setProjectId(projectId);
        CTask savedTask = taskRepo.save(task);

        CProject project = projectRepo.findById(projectId).orElseThrow();
        project.getTaskIds().add(savedTask.getTaskId());
        projectRepo.save(project);

        return savedTask;
    }

    // Get task list for project
    public List<CTask> getTasksForProject(String projectId) {
        return taskRepo.findByProjectId(projectId);
    }

    // Get projects assigned to manager
    public List<CProject> getProjectsByManager(String managerEmpId) {
        return projectRepo.findByManagerEmpId(managerEmpId);
    }

    // Get tasks assigned to an employee
    public List<CTask> getTasksForEmployee(String empId) {
        return taskRepo.findByAssignedEmpId(empId);
    }

    public CTask markTaskAsCompleted(String taskId, String employeeId) {
        CTask task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check if the task is assigned to the employee
        if (!task.getAssignedEmpId().equals(employeeId)) {
            throw new RuntimeException("You are not authorized to complete this task");
        }

        task.setStatus(TaskStatus.COMPLETED);
        return taskRepo.save(task);
    }
}
