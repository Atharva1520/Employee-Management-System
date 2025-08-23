package com.example.employee.management.dto;

public class PasswordChangeRequest {
    private String empId;       // Added
    private String oldPassword;
    private String newPassword;

    // Getters & Setters
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
