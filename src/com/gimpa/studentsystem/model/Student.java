package com.gimpa.studentsystem.model;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String program;
    private int yearOfStudy;

    // Default Constructor - create student empty object
    public Student() {}

    // Parameterized Constructor - create student with setters instead of direct assignment, makes validation runs automatically
    public Student(String studentId, String name, String email,
                   String phone, String program, int yearOfStudy) {
        setStudentId(studentId);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setProgram(program);
        setYearOfStudy(yearOfStudy);
    }

    // GETTERS - information to be seen from outside
    public String getStudentId() { return studentId; }
    public String getName()      { return name; }
    public String getEmail()     { return email; }
    public String getPhone()     { return phone; }
    public String getProgram()   { return program; }
    public int getYearOfStudy()  { return yearOfStudy; }

    //SETTERS with Validation - information that can be changed in the private fields from outside
    public void  setStudentId(String studentId) {
        if (studentId == null || studentId.trim() .isEmpty()) {     //reject empty student ID
            throw new IllegalArgumentException("Student ID cannot be Empty");
        }
        this.studentId = studentId.toUpperCase();
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student Name cannot be Empty");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        // reject empty email or one without @ and .
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.toLowerCase().trim();
    }


    public void setPhone(String phone) {
        // reject empty phone number
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
        this.phone = phone.trim();
    }


    public void setProgram(String program) {
        // reject empty program name
        if (program == null || program.trim().isEmpty()) {
            throw new IllegalArgumentException("Program cannot be empty");
        }
        this.program = program.trim();
    }


    public void setYearOfStudy(int yearOfStudy) {
        // year must be between 1 and 4
        if (yearOfStudy < 1 || yearOfStudy > 4) {
            throw new IllegalArgumentException("Year of study must be between 1 and 4");
        }
        this.yearOfStudy = yearOfStudy;
    }

    // Print student details
    public void displayInfo() {
        System.out.println("========================================");
        System.out.println("         STUDENT DETAILS                ");
        System.out.println("========================================");
        System.out.println("Student ID   : " + studentId);
        System.out.println("Name         : " + name);
        System.out.println("Email        : " + email);
        System.out.println("Phone        : " + phone);
        System.out.println("Program      : " + program);
        System.out.println("Year         : " + yearOfStudy);
        System.out.println("========================================");
    }

    // TO STRING - returns a single line summary of student
    @Override
    public String toString() {
        return studentId + " | " + name + " | " + program + " | Year " + yearOfStudy;
    }

    //EQUALS - compare two student objects if they have same ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId != null && studentId.equals(student.studentId);
    }

    // HASHCODE - required when using HashMap
    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
}
