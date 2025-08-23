package com.example.employee.management.config;

import com.example.employee.management.security.CEmployeeProfileSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CEmployeeProfileSecurity userDetailsService;

    public SecurityConfig(CEmployeeProfileSecurity userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Modern way to get AuthenticationManager from AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**", "/api/health").permitAll()

                        // Leave Management
                        .requestMatchers("/api/leave/approve/**").hasRole("MANAGER")
                        .requestMatchers("/api/leave/**").hasAnyRole("EMPLOYEE", "MANAGER")

                        // Payroll
                        .requestMatchers("/api/payroll/create/**").hasRole("MANAGER")
                        .requestMatchers("/api/payroll/**").hasAnyRole("EMPLOYEE", "MANAGER")

                        // Project Management
                        .requestMatchers("/api/projects/create").hasRole("MANAGER")
                        .requestMatchers("/api/projects/*/tasks").hasRole("MANAGER")   // add task
                        .requestMatchers("/api/projects/my-projects").hasRole("MANAGER") // manager’s own projects

                        .requestMatchers("/api/projects/**").hasAnyRole("EMPLOYEE", "MANAGER") // fetch tasks, my-tasks

                        // Default rule
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {}); // Use HTTP Basic Auth

        return http.build();
    }

}
