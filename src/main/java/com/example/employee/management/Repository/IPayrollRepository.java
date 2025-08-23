package com.example.employee.management.Repository;

import com.example.employee.management.model.CPayRoll;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPayrollRepository extends MongoRepository<CPayRoll, String> {

    List<CPayRoll> findByStrEmpId(String strEmpId);

    Optional<CPayRoll> findByStrEmpIdAndYearMonth(String strEmpId, String yearMonth);
}
