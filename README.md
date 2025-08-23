Employee Management System
Overview

The Employee Management System is a comprehensive web application designed to streamline and automate various HR and administrative tasks within an organization. Built using Java and Spring Boot, this system offers functionalities such as employee profile management, leave tracking, payroll processing, and project management.

Features

Employee Profiles: Maintain detailed records of employees, including personal information, roles, and departments.

Leave Management: Track and manage different types of leaves, including casual and sick leaves.

Payroll System: Automate salary calculations, deductions, and generate payslips.

Project Management: Assign tasks, monitor progress, and manage project timelines.

Authentication & Authorization: Secure login and role-based access control.

Notifications: Real-time alerts for leave approvals, task assignments, and other critical updates.

Technologies Used

Backend: Java, Spring Boot, Spring Security

Database: MySQL

Frontend: Thymeleaf (for server-side rendering)

Build Tool: Maven

Version Control: Git, GitHub

Installation
Prerequisites

Java 17 or higher

Maven 3.8+

MySQL 8.0+

Git

Steps

Clone the repository:

git clone https://github.com/Atharva1520/Employee-Management-System.git
cd Employee-Management-System


Configure the database:

Create a new MySQL database named employee_management.

Import the schema from src/main/resources/schema.sql to set up the necessary tables.

Set up application properties:

Duplicate src/main/resources/application.properties.example and rename it to application.properties.

Update the database connection details and other configurations as per your environment.

Build and run the application:

mvn clean install
mvn spring-boot:run


Access the application:

Open your browser and navigate to http://localhost:8080.

Usage

Admin Access: Use the default admin credentials to log in and manage employee data, leaves, payroll, and projects.

Employee Access: Employees can view their profiles, apply for leaves, and check their payroll information.

Contributing

Contributions are welcome! Please fork the repository, create a new branch, and submit a pull request with your proposed changes.
