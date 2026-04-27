package com.gimpa.studentsystem.model;

public class Student extends Person implements Manageable {

    // ===== STUDENT SPECIFIC FIELDS =====
    // name, email, phone are already in Person — no need to repeat them
    private String studentId;
    private String program;
    private int yearOfStudy;


    // ===== DEFAULT CONSTRUCTOR =====
    public Student() {
        super(); // calls Person's default constructor
        this.yearOfStudy = 1;
    }


    // ===== PARAMETERIZED CONSTRUCTOR =====
    public Student(String studentId, String name, String email,
                   String phone, String program, int yearOfStudy) {
        super(name, email, phone); // send name, email, phone UP to Person
        setStudentId(studentId);
        setProgram(program);
        setYearOfStudy(yearOfStudy);
    }

    // GETTERS - information to be seen from outside
    // ===== GETTERS =====
    public String getStudentId() { return studentId; }
    public String getProgram()   { return program; }
    public int getYearOfStudy()  { return yearOfStudy; }


    // ===== SETTERS WITH VALIDATION =====

    public void setStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty");
        }
        this.studentId = studentId.toUpperCase();
    }

    public void setProgram(String program) {
        if (program == null || program.trim().isEmpty()) {
            throw new IllegalArgumentException("Program cannot be empty");
        }
        this.program = program.trim();
    }

    public void setYearOfStudy(int yearOfStudy) {
        if (yearOfStudy < 1 || yearOfStudy > 4) {
            throw new IllegalArgumentException("Year of study must be between 1 and 4");
        }
        this.yearOfStudy = yearOfStudy;
    }


    // ===== IMPLEMENTING ABSTRACT METHODS FROM PERSON =====

    // tells the system this person's role is STUDENT
    @Override
    public String getRole() {
        return "STUDENT";
    }

    // displays full student information
    // calls displayCommonInfo() from Person first then adds student specific fields
    @Override
    public void displayInfo() {
        displayCommonInfo(); // prints role, name, email, phone from Person
        System.out.println("Student ID   : " + studentId);
        System.out.println("Program      : " + program);
        System.out.println("Year         : " + yearOfStudy);
        System.out.println("========================================");
    }

    // ===== STUDENT SPECIFIC METHODS =====

    // promotes student to next year
    public void promote() {
        if (yearOfStudy < 4) {
            yearOfStudy++;
            System.out.println(getName() + " promoted to Year " + yearOfStudy);
        } else {
            System.out.println(getName() + " is already in final year.");
        }
    }

    // checks if student qualifies for graduation
    public boolean isEligibleForGraduation() {
        return yearOfStudy == 4;
    }


    // ===== toString =====
    @Override
    public String toString() {
        return studentId + " | " + getName() + " | " + getProgram() + " | Year " + yearOfStudy;
    }


    // ===== equals =====
    // two students are equal if they have the same studentId
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId != null && studentId.equals(student.studentId);
    }


    // ===== hashCode =====
    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }


    @Override
    public void add() {
        System.out.println("Adding student: " + getName());
    }

    @Override
    public void view() {
        displayInfo();
    }

    @Override
    public void update() {
        System.out.println("Updating student: " + getName());
    }

    @Override
    public void delete() {
        System.out.println("Deleting student: " + getName());
    }

    @Override
    public boolean validate() {
        return getStudentId() != null && !getStudentId().isEmpty()
                && getName() != null && !getName().isEmpty()
                && getEmail() != null && getEmail().contains("@")
                && getProgram() != null && !getProgram().isEmpty()
                && getYearOfStudy() >= 1 && getYearOfStudy() <= 4;
    }

    @Override
    public String getSummary() {
        return String.format("%s: %s (%s) - Year %d",
                getStudentId(), getName(), getProgram(), getYearOfStudy());
    }
}
