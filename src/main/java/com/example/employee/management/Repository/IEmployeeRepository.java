package com.example.employee.management.Repository;

import com.example.employee.management.model.CEmployeeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEmployeeRepository extends MongoRepository<CEmployeeModel, Integer> {

    Optional<CEmployeeModel> findBystrEmpId(String strEmpId);

}
