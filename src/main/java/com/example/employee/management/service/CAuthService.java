package com.example.employee.management.service;

import com.example.employee.management.Repository.IEmployeeRepository;
import com.example.employee.management.model.CEmployeeModel;
import com.example.employee.management.model.CEmployeeProfile;
import com.example.employee.management.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CAuthService {

    private final IEmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public CAuthService(IEmployeeRepository employeeRepository, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String signUp(CEmployeeModel employee){
        Optional<CEmployeeModel> existing = employeeRepository.findBystrEmpId(employee.getStrEmpId());
        if(existing.isPresent()) return null;

        employee.setStrPassword(passwordEncoder.encode(employee.getStrPassword()));
        employeeRepository.save(employee);
        return "SignUp successful";
    }

    public String login(String empId, String dob, String pass){
        Optional<CEmployeeModel> emp = employeeRepository.findBystrEmpId(empId);
        if(emp.isPresent()){
            CEmployeeModel e = emp.get();
            if(e.getStrDateOfBirth().equals(dob) && passwordEncoder.matches(pass, e.getStrPassword())){
                return jwtUtil.generateToken(e.getStrEmpId(), e.getStrRole());
            }
        }
        return null;
    }
}
