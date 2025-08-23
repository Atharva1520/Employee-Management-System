package com.example.employee.management.service;

import com.example.employee.management.Repository.IEmployeeProfileRepository;
import com.example.employee.management.Repository.IEmployeeRepository;
import com.example.employee.management.Repository.IPayrollRepository;
import com.example.employee.management.model.CEmployeeModel;
import com.example.employee.management.model.CEmployeeProfile;
import com.example.employee.management.model.CPayRoll;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PayrollService {

    private final IPayrollRepository payrollRepo;
    private final IEmployeeRepository employeeRepo;
    private final IEmployeeProfileRepository profileRepository;

    public PayrollService(IPayrollRepository payrollRepo, IEmployeeRepository employeeRepo, IEmployeeProfileRepository profileRepository) {
        this.payrollRepo = payrollRepo;
        this.employeeRepo = employeeRepo;
        this.profileRepository = profileRepository;
    }

    public String createPayroll(CPayRoll payroll, String managerEmpId, String yearMonth) {
        CEmployeeModel manager = employeeRepo.findBystrEmpId(managerEmpId)
                .orElse(null);

        if (manager == null || !"MANAGER".equals(manager.getStrRole())) {
            return "Access denied: Only managers can create payroll.";
        }

        // set yearMonth from path variable
        payroll.setYearMonth(yearMonth);

        // calculate net pay
        payroll.setNetPay(
                payroll.getBasicSalary() + payroll.getHra() + payroll.getDa() - payroll.getDeductions()
        );

        payrollRepo.save(payroll);

        return "Payroll created successfully for " + payroll.getStrEmpId() +
                " for " + yearMonth;
    }


    // View salary breakdown (all months)
    public List<CPayRoll> getPayrollHistory(String empId) {
        return payrollRepo.findByStrEmpId(empId);
    }

    // View salary breakdown for a specific month
    public CPayRoll getPayrollForMonth(String empId, String yearMonth) {
        CPayRoll payroll = payrollRepo.findByStrEmpIdAndYearMonth(empId, yearMonth)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));
        return payroll;
    }

    public ByteArrayInputStream downloadPayslip(String empId, String yearMonth) {
        CPayRoll payroll = payrollRepo.findByStrEmpIdAndYearMonth(empId, yearMonth)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        CEmployeeModel employee = employeeRepo.findBystrEmpId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        CEmployeeProfile profile = profileRepository.findByStrEmpId(empId)
                .orElseThrow(() -> new RuntimeException("Employee profile not found"));

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Payslip - " + yearMonth, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Employee Info
            document.add(new Paragraph("Employee Name: " + employee.getStrName()));
            document.add(new Paragraph("Employee ID: " + employee.getStrEmpId()));
            document.add(new Paragraph("Department: " + profile.getDepartment()));
            document.add(new Paragraph("Designation: " + profile.getDesignation()));
            document.add(new Paragraph("Email: " + employee.getStrMail()));
            document.add(new Paragraph("\n"));

            // Salary Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            table.addCell("Basic Salary");
            table.addCell(String.valueOf(payroll.getBasicSalary()));

            table.addCell("HRA");
            table.addCell(String.valueOf(payroll.getHra()));

            table.addCell("DA");
            table.addCell(String.valueOf(payroll.getDa()));

            table.addCell("Deductions");
            table.addCell(String.valueOf(payroll.getDeductions()));

            table.addCell("Net Pay");
            table.addCell(String.valueOf(payroll.getNetPay()));

            document.add(table);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating payslip PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
