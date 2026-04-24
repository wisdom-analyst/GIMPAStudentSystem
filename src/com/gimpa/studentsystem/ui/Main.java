package com.gimpa.studentsystem.ui;

//import Arraylist
import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.service.DataStore;

import java.util.ArrayList;
import java.util.Scanner;

// Main class - Entry point for the GIMPA Student Management System - Handles all menu display and user interaction

public class Main {

    // input reader
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   GIMPA STUDENT MANAGEMENT SYSTEM   ║");
        System.out.println("║   OBJECT ORIENTED PROGRAMMING 1     ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("   Welcome! Please select an option.   ");

        int choice;

        // Main Loop
        do {
            displayMenu();  // show the menu options
            choice = getIntInput ("Enter your choice: ");

            // Each case calls a method that handles that menu option
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addCourse();
                    break;
                case 3:
                    viewAllStudents();
                    break;
                case 4:
                    viewAllCourses();
                    break;
                case 5:
                    searchStudent();
                    break;
                case 6:
                    updateStudent();
                    break;
                case 7:
                    deleteStudent();
                    break;
                case 8:
                    DataStore.displaySummary();
                    break;
                case 9:
                    System.out.println("\nThank you for using GIMPA SMS. Goodbye!");
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid choice. Enter a number between 1-9.");
            }

        } while (choice != 9);

        scanner.close(); // release the scanner resource when program ends
    }
//=== MENU ===
private static void displayMenu() {
    System.out.println("\n╔══════════════════════════════════════╗");
    System.out.println("║             MAIN MENU                ║");
    System.out.println("╠══════════════════════════════════════╣");
    System.out.println("║  1. Add New Student                  ║");
    System.out.println("║  2. Add New Course                   ║");
    System.out.println("║  3. View All Students                ║");
    System.out.println("║  4. View All Courses                 ║");
    System.out.println("║  5. Search Student by Name           ║");
    System.out.println("║  6. Update Student                   ║");
    System.out.println("║  7. Delete Student                   ║");
    System.out.println("║  8. View System Summary              ║");
    System.out.println("║  9. Exit                             ║");
    System.out.println("╚══════════════════════════════════════╝");
}

// ====== ADD STUDENT =======
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

            System.out.print("Enter Year (1-4)    : ");
            int year = getIntInput("");

            // Create student object - setters inside constructor validate the data
            Student student = new Student(studentId, name, email, phone, program, year);

            // Send to DataStore - returns false if ID already exists
            if (DataStore.addStudent(student)) {
                System.out.println("[SUCCESS] Student added successfully!");
            }

        } catch (IllegalArgumentException e) {
            // Catches any validation error thrown by the Student setters
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


// ADD COURSE - Collect course details from user and adds to the list
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

        // Create course object - setters validate the data
        Course course = new Course(courseCode, courseTitle, credits, instructor);

        // Send to DataStore
        if (DataStore.addCourse(course)) {
            System.out.println("[SUCCESS] Course added successfully!");
        }

    } catch (IllegalArgumentException e) {
        System.out.println("[ERROR] " + e.getMessage());
    }
}


// VIEW ALL STUDENTS
    private static void viewAllStudents() {
        System.out.println("\n===== ALL STUDENTS =====");

        ArrayList<Student> students = DataStore.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found. Add a student first.");
            return; // stop the method here - nothing else to do
        }

        System.out.println("Total Students: " + students.size());
        System.out.println("----------------------------------------");

        // Call each student's displayInfo() to print their details
        for (Student student : students) {
            student.displayInfo();
        }
    }

 // VIEW ALL COURSES - Loop through the courses list and display each one
 private static void viewAllCourses() {
     System.out.println("\n===== ALL COURSES =====");

     ArrayList<Course> courses = DataStore.getAllCourses();

     if (courses.isEmpty()) {
         System.out.println("No courses found. Add a course first.");
         return;
     }

     System.out.println("Total Courses: " + courses.size());
     System.out.println("----------------------------------------");

     for (Course course : courses) {
         course.displayInfo();
     }
 }


 // SEARCH - Asks user for a name and searches DataStore for matches
    private static void searchStudent() {
        System.out.println("\n===== SEARCH STUDENT =====");
        System.out.print("Enter name to search: ");
        String searchName = scanner.nextLine();

        ArrayList<Student> results = DataStore.searchStudentsByName(searchName);

        if (results.isEmpty()) {
            System.out.println("No students found matching '" + searchName + "'");
        } else {
            System.out.println("Found " + results.size() + " student(s):");
            for (Student student : results) {
                student.displayInfo();
            }
        }
    }


    // UPDATE STUDENT
    /**
     * Finds a student by ID, shows current details,
     * then lets the user update individual fields.
     * Pressing Enter without typing keeps the existing value.
     */

    private static void updateStudent() {
        System.out.println("\n===== UPDATE STUDENT =====");
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine();

        // Try to find the student first
        Student existing = DataStore.getStudentById(studentId);

        if (existing == null) {
            System.out.println("[ERROR] Student not found.");
            return;
        }

        System.out.println("Current details:");
        existing.displayInfo();

        try {
            System.out.println("Press Enter to keep current value.");

            // Show current value in brackets - user can type new or press Enter to skip
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

            // Save the updated student back to DataStore
            DataStore.updateStudent(existing);

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // DELETE STUDENT
    /**
     * Finds a student by ID, shows their details,
     * then asks for confirmation before permanently deleting.
     */
    private static void deleteStudent() {
        System.out.println("\n===== DELETE STUDENT =====");
        System.out.print("Enter Student ID to delete: ");
        String studentId = scanner.nextLine();

        Student student = DataStore.getStudentById(studentId);

        if (student == null) {
            System.out.println("[ERROR] Student not found.");
            return;
        }

        // Show who is about to be deleted so user can confirm
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


    // INT INPUT - Program react to text inout instead of number
    private static int getIntInput(String prompt) {
        System.out.print(prompt);

        // hasNextInt() returns false if next input is not a number
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next(); // discard the invalid input and try again
        }

        int input = scanner.nextInt();
        scanner.nextLine(); // consume the leftover newline character
        return input;
    }

}