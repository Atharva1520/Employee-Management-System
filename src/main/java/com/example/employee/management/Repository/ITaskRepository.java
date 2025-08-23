package com.example.employee.management.Repository;

import com.example.employee.management.model.CTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITaskRepository extends MongoRepository<CTask, String> {
    List<CTask> findByProjectId(String projectId);
    List<CTask> findByAssignedEmpId(String empId);
}
