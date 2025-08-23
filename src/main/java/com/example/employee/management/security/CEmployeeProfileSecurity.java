package com.example.employee.management.security;

import com.example.employee.management.Repository.IEmployeeRepository;
import com.example.employee.management.model.CEmployeeModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CEmployeeProfileSecurity implements UserDetailsService {

    private final IEmployeeRepository employeeRepository;

    public CEmployeeProfileSecurity(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String empId) throws UsernameNotFoundException {
        CEmployeeModel employee = employeeRepository.findBystrEmpId(empId)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        String role = employee.getStrRole().toUpperCase(); // e.g., "EMP" or "MANAGER"

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + role));

        return new User(employee.getStrEmpId(), employee.getStrPassword(), authorities);
    }

}
