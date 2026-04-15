package com.gimpa.studentsystem.model;

public class Enrollment {
    Student student;
    Course course;
    double grade;
    boolean isGraded;

    // Empty Constructor - set default values for grades fields
    public Enrollment() {
        this.isGraded = false;      // no grade by default
        this.grade = 0;             // grade start at zero at default
    }

    // Constructor - Links Student to a Course
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.isGraded = false;       // grade not recorded yet
        this.grade = 0;              // start at zero
    }

    // Print Enrollment details
    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("            ENROLLMENT  DETAILS     ");
        System.out.println("========================================");
        System.out.println("Student :" + student.name);
        System.out.println("Course :" + course.courseTitle);

        if  (isGraded) {
            System.out.println("Grade :" + grade);
        } else  {
            System.out.println("Grade : Not yet graded");
        }

    }


}
