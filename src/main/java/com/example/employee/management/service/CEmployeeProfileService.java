package com.example.employee.management.service;

import com.example.employee.management.Repository.IEmployeeRepository;
import com.example.employee.management.Repository.IEmployeeProfileRepository;
import com.example.employee.management.model.CEmployeeModel;
import com.example.employee.management.model.CEmployeeProfile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CEmployeeProfileService {

    private final IEmployeeProfileRepository profileRepository;
    private final IEmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder; // Use interface

    public CEmployeeProfileService(
            IEmployeeProfileRepository profileRepository,
            IEmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<CEmployeeProfile> viewProfile(String empId){
        return profileRepository.findByStrEmpId(empId);
    }

    public CEmployeeProfile updateProfile(String empId, CEmployeeProfile updatedProfile){
        Optional<CEmployeeProfile> existing = profileRepository.findByStrEmpId(empId);
        if(existing.isPresent()){
            CEmployeeProfile profile = existing.get();
            profile.setAddress(updatedProfile.getAddress());
            profile.setPhoneNumber(updatedProfile.getPhoneNumber());
            profile.setDepartment(updatedProfile.getDepartment());
            profile.setDesignation(updatedProfile.getDesignation());

            return profileRepository.save(profile);
        }
        updatedProfile.setStrEmpId(empId);
        return profileRepository.save(updatedProfile);
    }

    public boolean changePassword(String empId, String oldPass, String newPass){
        Optional<CEmployeeModel> employee = employeeRepository.findBystrEmpId(empId);
        if(employee.isEmpty()) return false;

        CEmployeeModel existingEmployee = employee.get();

        if(!passwordEncoder.matches(oldPass, existingEmployee.getStrPassword())){
            return false;
        }

        existingEmployee.setStrPassword(passwordEncoder.encode(newPass));
        employeeRepository.save(existingEmployee);
        return true;
    }
}
