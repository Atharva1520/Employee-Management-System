package com.example.employee.management.Repository;

import com.example.employee.management.model.CEmployeeProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IEmployeeProfileRepository extends MongoRepository<CEmployeeProfile, String> {
    Optional<CEmployeeProfile> findByStrEmpId(String empId);
}
