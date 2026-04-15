package com.gimpa.studentsystem.model;

public class Student {
    String studentId;
    String name;
    String email;
    String phone;

    // Default Constructor - create student empty object
    public Student() {}

    // Parameterized Constructor - create student with full details
    public Student(String studentId, String name, String email, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Print student details
    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("            STUDENT DETAILS             ");
        System.out.println("========================================");
        System.out.println("Student ID: " + this.studentId);
        System.out.println("Name: " + this.name);
        System.out.println("Email: " + this.email);
        System.out.println("Phone: " + this.phone);
        System.out.println("========================================");
    }
}
