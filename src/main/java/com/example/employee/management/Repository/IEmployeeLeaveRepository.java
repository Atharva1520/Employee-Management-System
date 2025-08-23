package com.example.employee.management.Repository;

import com.example.employee.management.model.CEmployeeLeave;
import com.example.employee.management.model.LeaveType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IEmployeeLeaveRepository extends MongoRepository<CEmployeeLeave,String> {

    List<CEmployeeLeave> findByStrEmpId(String strEmpId);

}
