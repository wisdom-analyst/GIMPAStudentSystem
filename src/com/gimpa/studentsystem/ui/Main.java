package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Instructor;
import com.gimpa.studentsystem.model.Person;
import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.service.DataStore;
import com.gimpa.studentsystem.service.EnrollmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Main class - Entry point for the GIMPA Student Management System.

public class Main {

    private static final Scanner scanner = new Scanner(System.in);


    /**
     * Program entry point. Loops the menu until user exits.
     */
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   GIMPA STUDENT MANAGEMENT SYSTEM   ║");
        System.out.println("║   OBJECT ORIENTED PROGRAMMING 1     ║");
        System.out.println("╚══════════════════════════════════════╝");

        int choice;

        do {
            displayMenu();
            choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:  addStudent();           break;
                case 2:  addCourse();            break;
                case 3:  viewAllStudents();      break;
                case 4:  viewAllCourses();       break;
                case 5:  searchStudent();        break;
                case 6:  updateStudent();        break;
                case 7:  deleteStudent();        break;
                case 8:  enrollStudent();        break;
                case 9:  recordGrade();          break;
                case 10: viewStudentEnrollments(); break;
                case 11: viewCourseRoster();     break;
                case 12: addInstructor();        break;
                case 13: polymorphismDemo();     break;
                case 14: DataStore.displaySummary(); break;
                case 15:
                    System.out.println("\nThank you for using GIMPA SMS. Goodbye!");
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid choice. Enter 1-15.");
            }

        } while (choice != 15);

        scanner.close();
    }


    // ===== MENU =====

    private static void displayMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║             MAIN MENU                ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  --- STUDENT MANAGEMENT ---          ║");
        System.out.println("║  1.  Add New Student                 ║");
        System.out.println("║  2.  Add New Course                  ║");
        System.out.println("║  3.  View All Students               ║");
        System.out.println("║  4.  View All Courses                ║");
        System.out.println("║  5.  Search Student by Name          ║");
        System.out.println("║  6.  Update Student                  ║");
        System.out.println("║  7.  Delete Student                  ║");
        System.out.println("║  --- ENROLLMENT & GRADES ---         ║");
        System.out.println("║  8.  Enroll Student in Course        ║");
        System.out.println("║  9.  Record Grade                    ║");
        System.out.println("║  10. View Student Enrollments        ║");
        System.out.println("║  11. View Course Roster              ║");
        System.out.println("║  --- STAFF & SYSTEM ---              ║");
        System.out.println("║  12. Add Instructor                  ║");
        System.out.println("║  13. Polymorphism Demo               ║");
        System.out.println("║  14. View System Summary             ║");
        System.out.println("║  15. Exit                            ║");
        System.out.println("╚══════════════════════════════════════╝");
    }


    // ===== STUDENT OPERATIONS =====
    private static void addStudent() {
        System.out.println("\n==== ADD NEW STUDENT ====");
        try {
            System.out.print("Enter Student ID    : ");
            String studentId = scanner.nextLine();

            System.out.print("Enter Student Name  : ");
            String name = scanner.nextLine();

            System.out.print("Enter Email         : ");
            String email = scanner.nextLine();

            System.out.print("Enter Phone         : ");
            String phone = scanner.nextLine();

            System.out.print("Enter Program       : ");
            String program = scanner.nextLine();

            int year = getIntInput("Enter Year (1-4)    : ");

            Student student = new Student(studentId, name, email, phone, program, year);

            if (DataStore.addStudent(student)) {
                System.out.println("[SUCCESS] Student added successfully!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void addCourse() {
        System.out.println("\n==== ADD NEW COURSE ====");
        try {
            System.out.print("Enter Course Code   : ");
            String courseCode = scanner.nextLine();

            System.out.print("Enter Course Title  : ");
            String courseTitle = scanner.nextLine();

            int credits = getIntInput("Enter Credits (1-6) : ");

            System.out.print("Enter Instructor    : ");
            String instructor = scanner.nextLine();

            Course course = new Course(courseCode, courseTitle, credits, instructor);

            if (DataStore.addCourse(course)) {
                System.out.println("[SUCCESS] Course added successfully!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void viewAllStudents() {
        System.out.println("\n===== ALL STUDENTS =====");
        ArrayList<Student> students = DataStore.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found. Add a student first.");
            return;
        }

        System.out.println("Total Students: " + students.size());
        System.out.println("----------------------------------------");
        for (Student s : students) {
            s.displayInfo();
        }
    }

    private static void viewAllCourses() {
        System.out.println("\n===== ALL COURSES =====");
        ArrayList<Course> courses = DataStore.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found. Add a course first.");
            return;
        }

        System.out.println("Total Courses: " + courses.size());
        System.out.println("----------------------------------------");
        for (Course c : courses) {
            c.displayInfo();
        }
    }

    private static void searchStudent() {
        System.out.println("\n===== SEARCH STUDENT =====");
        System.out.print("Enter name to search: ");
        String searchName = scanner.nextLine();

        ArrayList<Student> results = DataStore.searchStudentsByName(searchName);

        if (results.isEmpty()) {
            System.out.println("No students found matching '" + searchName + "'");
        } else {
            System.out.println("Found " + results.size() + " student(s):");
            for (Student s : results) {
                s.displayInfo();
            }
        }
    }

    private static void updateStudent() {
        System.out.println("\n===== UPDATE STUDENT =====");
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine();

        Student existing = DataStore.getStudentById(studentId);
        if (existing == null) {
            System.out.println("[ERROR] Student not found.");
            return;
        }

        System.out.println("Current details:");
        existing.displayInfo();

        try {
            System.out.println("Press Enter to keep current value.");

            System.out.print("Name [" + existing.getName() + "]: ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) existing.setName(name);

            System.out.print("Email [" + existing.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) existing.setEmail(email);

            System.out.print("Phone [" + existing.getPhone() + "]: ");
            String phone = scanner.nextLine();
            if (!phone.isEmpty()) existing.setPhone(phone);

            System.out.print("Program [" + existing.getProgram() + "]: ");
            String program = scanner.nextLine();
            if (!program.isEmpty()) existing.setProgram(program);

            DataStore.updateStudent(existing);

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        System.out.println("\n===== DELETE STUDENT =====");
        System.out.print("Enter Student ID to delete: ");
        String studentId = scanner.nextLine();

        Student student = DataStore.getStudentById(studentId);
        if (student == null) {
            System.out.println("[ERROR] Student not found.");
            return;
        }

        System.out.println("Student to delete:");
        student.displayInfo();

        System.out.print("Are you sure? (Y/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            DataStore.deleteStudent(studentId);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }


    // ===== ENROLLMENT OPERATIONS =====
    private static void enrollStudent() {
        System.out.println("\n===== ENROLL STUDENT =====");
        System.out.print("Enter Student ID  : ");
        String studentId = scanner.nextLine();

        System.out.print("Enter Course Code : ");
        String courseCode = scanner.nextLine();

        // enrollStudent() returns true/false and prints its own messages
        EnrollmentService.enrollStudent(studentId, courseCode);
    }

    // Records a percentage grade (0-100) for a student's course.
    private static void recordGrade() {
        System.out.println("\n===== RECORD GRADE =====");
        System.out.print("Enter Student ID  : ");
        String studentId = scanner.nextLine();

        System.out.print("Enter Course Code : ");
        String courseCode = scanner.nextLine();

        double grade = getDoubleInput("Enter Grade (0-100): ");

        EnrollmentService.recordGrade(studentId, courseCode, grade);
    }

    // Shows all courses a student is enrolled in, with grades.

    private static void viewStudentEnrollments() {
        System.out.println("\n===== STUDENT ENROLLMENTS =====");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        // Verify the student exists before proceeding
        Student student = DataStore.getStudentById(studentId);
        if (student == null) {
            System.out.println("[ERROR] Student not found.");
            return;
        }

        // Display the full transcript through EnrollmentService
        EnrollmentService.displayTranscript(studentId);
    }

    // Shows all students enrolled in a specific course, with grades.
    private static void viewCourseRoster() {
        System.out.println("\n===== COURSE ROSTER =====");
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().toUpperCase();

        // Verify the course exists
        Course course = DataStore.getCourseByCode(courseCode);
        if (course == null) {
            System.out.println("[ERROR] Course not found.");
            return;
        }

        System.out.println("\nCourse: " + course.getCourseTitle());
        System.out.println("Instructor: " + course.getInstructor());
        System.out.println("----------------------------------------");

        List<Student> enrolled = EnrollmentService.getCourseStudents(courseCode);
        if (enrolled.isEmpty()) {
            System.out.println("No students enrolled in this course yet.");
            return;
        }

        System.out.println("Enrolled Students: " + enrolled.size());
        for (Student s : enrolled) {
            System.out.println("  - " + s.getStudentId() + " | " + s.getName());
        }
    }


    // ===== INSTRUCTOR =====

    // Adds a new instructor to DataStore.
    private static void addInstructor() {
        System.out.println("\n===== ADD INSTRUCTOR =====");
        try {
            System.out.print("Enter Employee ID   : ");
            String employeeId = scanner.nextLine();

            System.out.print("Enter Name          : ");
            String name = scanner.nextLine();

            System.out.print("Enter Email         : ");
            String email = scanner.nextLine();

            System.out.print("Enter Phone         : ");
            String phone = scanner.nextLine();

            System.out.print("Enter Department    : ");
            String department = scanner.nextLine();

            System.out.print("Enter Title (Dr./Prof./Mr./Ms./Mrs.): ");
            String title = scanner.nextLine();

            Instructor instructor = new Instructor(
                    employeeId, name, email, phone, department, title);

            if (DataStore.addInstructor(instructor)) {
                System.out.println("[SUCCESS] Instructor added successfully!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


    // ===== POLYMORPHISM DEMO =====

    // Demonstrates polymorphism - the core Phase 3 concept.

    private static void polymorphismDemo() {
        System.out.println("\n===== POLYMORPHISM DEMO =====");
        System.out.println("Creating a Student and Instructor as Person references...\n");

        // Both variables are declared as type Person
        // but hold different actual object types
        Person student = new Student(
                "DEMO001", "Ama Owusu", "ama@gimpa.edu.gh",
                "0241234567", "Computer Science", 2);

        Person instructor = new Instructor(
                "EMP001", "Kofi Asante", "kofi@gimpa.edu.gh",
                "0209876543", "Computer Science", "Dr.");

        System.out.println("--- Calling printPersonDetails() on BOTH ---");
        System.out.println("Same method, different behaviour based on actual type:\n");

        // One method handles both types - this is polymorphism in action
        printPersonDetails(student);
        printPersonDetails(instructor);

        // Array holding mixed Person types
        System.out.println("\n--- Looping through a Person array ---");
        Person[] people = {student, instructor};
        for (Person p : people) {
            // getRole() returns "STUDENT" or "INSTRUCTOR"
            // Java picks the right version at runtime
            System.out.println(p.getRole() + " → " + p.getName());
        }
    }

    // Accepts ANY Person object - Student, Instructor, or any future subclass.
    private static void printPersonDetails(Person person) {
        System.out.println("Role: " + person.getRole());
        System.out.println("Name: " + person.getName());
        person.displayInfo(); // correct version called automatically
        System.out.println();
    }


    // ===== HELPER METHODS =====

    // Safely reads an integer. Keeps asking if user types non-numeric input.

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return input;
    }

    // Safely reads a decimal number. Used for grade entry.
    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine();
        return input;
    }

}