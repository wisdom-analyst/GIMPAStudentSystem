package com.gimpa.studentsystem.model;

public class Course {
    String courseCode;
    String courseTitle;
    int credits;
    String instructor;

    // Default Constrictor - create an empty course object
    public Course() {}

    // Parameterized constructor - create a course with full details / /
    public Course(String courseCode, String courseTitle, int credits, String instructor) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.credits = credits;
        this.instructor = instructor;
    }

    // Print Course Details
    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("            COURSE DETAILS              ");
        System.out.println("========================================");
        System.out.println("Course Code: " + courseCode);
        System.out.println("Course Title: " + courseTitle);
        System.out.println("Credits: " + credits);
        System.out.println("Instructor: " + instructor);
        System.out.println("========================================");
    }
}
