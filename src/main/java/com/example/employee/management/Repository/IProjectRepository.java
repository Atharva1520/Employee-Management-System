package com.example.employee.management.Repository;

import com.example.employee.management.model.CProject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IProjectRepository extends MongoRepository<CProject, String> {
    List<CProject> findByManagerEmpId(String managerEmpId);
}
