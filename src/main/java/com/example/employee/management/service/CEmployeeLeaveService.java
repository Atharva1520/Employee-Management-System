package com.example.employee.management.service;

import com.example.employee.management.Repository.IEmployeeLeaveRepository;
import com.example.employee.management.Repository.IEmployeeRepository;
import com.example.employee.management.model.CEmployeeLeave;
import com.example.employee.management.model.CEmployeeModel;
import com.example.employee.management.model.LeaveType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CEmployeeLeaveService {

    private final IEmployeeLeaveRepository leaveRepository;
    private final IEmployeeRepository employeeRepository;
    // Default leave balances
    private  static final int DEFAULT_SICK_LEAVE = 5;
    private static final int DEFAULT_CASUAL_LEAVE = 2;

    public CEmployeeLeaveService(IEmployeeLeaveRepository leaveRepository, IEmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    public String applyLeave(CEmployeeLeave leaveRequest) {
        // Fetch latest record of this employee (if exists)
        List<CEmployeeLeave> existingLeaves = leaveRepository.findByStrEmpId(leaveRequest.getStrEmpId());
        CEmployeeLeave latestRecord = existingLeaves.isEmpty() ? null : existingLeaves.get(existingLeaves.size() - 1);

        // Get current balances
        int sickBalance = (latestRecord != null) ? latestRecord.getRemainingSickLeave() : DEFAULT_SICK_LEAVE;
        int casualBalance = (latestRecord != null) ? latestRecord.getRemainingCasualLeave() : DEFAULT_CASUAL_LEAVE;

        // Deduct based on leave type
        if (leaveRequest.getLeaveType() == LeaveType.SICK) {
            if (sickBalance < leaveRequest.getDaysApplied()) {
                return "Insufficient Sick Leave balance!";
            }
            sickBalance -= leaveRequest.getDaysApplied();
        } else if (leaveRequest.getLeaveType() == LeaveType.CASUAL) {
            if (casualBalance < leaveRequest.getDaysApplied()) {
                return "Insufficient Casual Leave balance!";
            }
            casualBalance -= leaveRequest.getDaysApplied();
        }

        // Update balances in the same document
        leaveRequest.setRemainingSickLeave(sickBalance);
        leaveRequest.setRemainingCasualLeave(casualBalance);
        leaveRequest.setStatus("PENDING");

        // Save just this one leave request (single entry per leave)
        leaveRepository.save(leaveRequest);

        return "Leave applied successfully!";
    }


    public List<CEmployeeLeave> getLeaves(String empId) {
        return leaveRepository.findByStrEmpId(empId);
    }

    public String approveLeave(String leaveId, String managerEmpId, boolean approve) {
        // Fetch the manager details
        CEmployeeModel manager = employeeRepository.findBystrEmpId(managerEmpId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        if (!"MANAGER".equalsIgnoreCase(manager.getStrRole())) {
            return "Access denied! Only managers can approve/reject leave.";
        }

        // Find leave request
        CEmployeeLeave leaveRequest = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!"PENDING".equals(leaveRequest.getStatus())) {
            return "Leave already processed!";
        }

        if (approve) {
            leaveRequest.setStatus("APPROVED");
        } else {
            // refund balance
            if (leaveRequest.getLeaveType() == LeaveType.SICK) {
                leaveRequest.setRemainingSickLeave(leaveRequest.getRemainingSickLeave() + leaveRequest.getDaysApplied());
            } else if (leaveRequest.getLeaveType() == LeaveType.CASUAL) {
                leaveRequest.setRemainingCasualLeave(leaveRequest.getRemainingCasualLeave() + leaveRequest.getDaysApplied());
            }
            leaveRequest.setStatus("REJECTED");
        }

        leaveRepository.save(leaveRequest);
        return approve ? "Leave approved successfully!" : "Leave rejected and balance refunded!";
    }

}
