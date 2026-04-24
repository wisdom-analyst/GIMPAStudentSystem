package com.gimpa.studentsystem.model;

// PRIVATE FIELDS
public class Enrollment {
    private Student student;
    private Course course;
    private double grade;
    private boolean isGraded;

    // Empty Constructor - set default values for grades fields
    public Enrollment() {
        this.isGraded = false;      // no grade by default
        this.grade = 0;             // grade start at zero at default
    }

    // Parameterized Constructor - Links Student to a Course
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.isGraded = false;       // grade not recorded yet
        this.grade = 0;              // start at zero
    }

    //GETTERS - return student object linked to this enrolment
    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public double getGrade() {
        return grade;
    }

    public boolean isGraded() {
        return isGraded;
    }

    // SETTERS WITH VALIDATION
    public void setStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        this.student = student;
    }

    public void setCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        this.course = course;
    }

    public void setGrade(double grade) {
        if (grade < 0.0 || grade > 100.0) {
            throw new IllegalArgumentException(
                    "Grade must be between 0 and 100. Entered: " + grade);
        }
        this.grade = grade;
        this.isGraded = true; // grade is now officially recorded
    }

    public void setGraded(boolean isGraded) {
        this.isGraded = isGraded;
    }

    // Print Enrollment details
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.println("│        ENROLLMENT DETAILS           │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.printf( "│ Student : %-25s │%n", student.getName());
        System.out.printf( "│ Course  : %-25s │%n", course.getCourseTitle());

        // Show grade only if it has been recorded, otherwise show "Pending"
        if (isGraded) {
            System.out.printf("│ Grade   : %-25.1f │%n", grade);
        } else {
            System.out.printf("│ Grade   : %-25s │%n", "Pending");
        }

        System.out.println("└─────────────────────────────────────┘");
    }

    //UTILITY METHODS - to prevent duplicate enrolment
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enrollment enrollment = (Enrollment) obj;

        // Same student ID AND same course code = duplicate enrollment
        return student.getStudentId().equals(enrollment.student.getStudentId())
                && course.getCourseCode().equals(enrollment.course.getCourseCode());
    }

    // HASH CODE
    @Override
    public int hashCode() {
        int result = student != null ? student.getStudentId().hashCode() : 0;
        result = 31 * result + (course != null ? course.getCourseCode().hashCode() : 0);
        return result;
    }
    }

