package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.*;
import com.gimpa.studentsystem.service.*;
import com.gimpa.studentsystem.exception.EntityNotFoundException;
import com.gimpa.studentsystem.util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class — console entry point for the GIMPA system.
 * Handles all user interactions through the command line.
 * Phase 5 - Exception Handling and File I/O
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    static void main() {
        // load existing data from file on startup
        FileManager.loadData();

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   GIMPA STUDENT MANAGEMENT SYSTEM    ║");
        System.out.println("║   PHASE 5: EXCEPTIONS & FILES        ║");
        System.out.println("╚══════════════════════════════════════╝");

        int choice = 0;

        do {
            try {
                displayMenu();
                choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:  addStudent();              break;
                    case 2:  addCourse();               break;
                    case 3:  viewAllStudents();         break;
                    case 4:  viewAllCourses();          break;
                    case 5:  searchStudent();           break;
                    case 6:  updateStudent();           break;
                    case 7:  deleteStudent();           break;
                    case 8:  enrollStudent();           break;
                    case 9:  recordGrade();             break;
                    case 10: viewStudentEnrollments();  break;
                    case 11: viewCourseRoster();        break;
                    case 12: addInstructor();           break;
                    case 13: polymorphismDemo();        break;
                    case 14: DataStore.displaySummary(); break;
                    case 15:
                        FileManager.saveData(); // save before exit
                        System.out.println("\nData saved. Goodbye!");
                        break;
                    default:
                        System.out.println("\n[ERROR] Invalid choice. Enter 1-15.");
                }
            } catch (Exception e) {
                System.out.println("[CRITICAL ERROR] " + e.getMessage());
                scanner.nextLine();
            }

        } while (choice != 15);

        scanner.close();
    }


    private static void displayMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║             MAIN MENU              ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║  1. Add Student    | 8.  Enroll    ║");
        System.out.println("║  2. Add Course     | 9.  Grade     ║");
        System.out.println("║  3. View Students  | 10. Transcript║");
        System.out.println("║  4. View Courses   | 11. Roster    ║");
        System.out.println("║  5. Search Student | 12. Instructor║");
        System.out.println("║  6. Update Student | 13. Poly Demo ║");
        System.out.println("║  7. Delete Student | 14. Summary   ║");
        System.out.println("║  15. SAVE & EXIT                   ║");
        System.out.println("╚════════════════════════════════════╝");
    }


    private static void addStudent() {
        System.out.println("\n==== ADD NEW STUDENT ====");
        try {
            System.out.print("ID: ");      String id    = scanner.nextLine();
            System.out.print("Name: ");    String name  = scanner.nextLine();
            System.out.print("Email: ");   String email = scanner.nextLine();
            System.out.print("Phone: ");   String phone = scanner.nextLine();
            System.out.print("Program: "); String prog  = scanner.nextLine();
            int year = getIntInput("Year (1-4): ");

            Student s = new Student(id, name, email, phone, prog, year);
            if (DataStore.addStudent(s)) {
                FileManager.saveData(); // auto-save after adding
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


    private static void addCourse() {
        System.out.println("\n==== ADD NEW COURSE ====");
        try {
            System.out.print("Code: ");       String code  = scanner.nextLine();
            System.out.print("Title: ");      String title = scanner.nextLine();
            int creds = getIntInput("Credits: ");
            System.out.print("Instructor: "); String inst  = scanner.nextLine();

            if (DataStore.addCourse(new Course(code, title, creds, inst))) {
                FileManager.saveData();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


    private static void viewAllStudents() {
        ArrayList<Student> students = DataStore.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        for (Student s : students) s.displayInfo();
    }


    private static void viewAllCourses() {
        ArrayList<Course> courses = DataStore.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        for (Course c : courses) c.displayInfo();
    }


    private static void searchStudent() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        ArrayList<Student> results = DataStore.searchStudentsByName(name);
        if (results.isEmpty()) {
            System.out.println("No students found matching: " + name);
            return;
        }
        for (Student s : results) s.displayInfo();
    }


    private static void updateStudent() {
        System.out.println("\n===== UPDATE STUDENT =====");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        try {
            Student existing = DataStore.getStudentById(studentId);
            existing.displayInfo();

            System.out.println("Press Enter to keep current value.");
            System.out.print("Name [" + existing.getName() + "]: ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) existing.setName(name);

            System.out.print("Program [" + existing.getProgram() + "]: ");
            String program = scanner.nextLine();
            if (!program.isEmpty()) existing.setProgram(program);

            DataStore.updateStudent(existing);
            FileManager.saveData();

        } catch (EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


    private static void deleteStudent() {
        System.out.println("\n===== DELETE STUDENT =====");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        try {
            Student student = DataStore.getStudentById(studentId);
            student.displayInfo();
            System.out.print("Are you sure? (Y/N): ");
            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                DataStore.deleteStudent(studentId);
                FileManager.saveData();
            }
        } catch (EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


    private static void enrollStudent() {
        System.out.print("Student ID: ");  String sId   = scanner.nextLine();
        System.out.print("Course Code: "); String cCode = scanner.nextLine();
        EnrollmentService.enrollStudent(sId, cCode);
        FileManager.saveData();
    }


    private static void recordGrade() {
        System.out.print("Student ID: ");  String sId   = scanner.nextLine();
        System.out.print("Course Code: "); String cCode = scanner.nextLine();
        double grade = getDoubleInput("Grade (0-100): ");
        EnrollmentService.recordGrade(sId, cCode, grade);
        FileManager.saveData();
    }


    private static void viewStudentEnrollments() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        try {
            DataStore.getStudentById(id);
            EnrollmentService.displayTranscript(id);
        } catch (EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


    private static void viewCourseRoster() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().toUpperCase();
        try {
            Course course = DataStore.getCourseByCode(code);
            System.out.println("\nRoster for: " + course.getCourseTitle());
            List<Student> enrolled = EnrollmentService.getCourseStudents(code);
            if (enrolled.isEmpty()) {
                System.out.println("No students enrolled.");
                return;
            }
            for (Student s : enrolled) System.out.println(" - " + s.getName());
        } catch (EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


    private static void addInstructor() {
        System.out.println("\n==== ADD INSTRUCTOR ====");
        try {
            System.out.print("ID: ");     String id    = scanner.nextLine();
            System.out.print("Name: ");   String name  = scanner.nextLine();
            System.out.print("Email: ");  String email = scanner.nextLine();
            System.out.print("Phone: ");  String phone = scanner.nextLine();
            System.out.print("Dept: ");   String dept  = scanner.nextLine();
            System.out.print("Title: ");  String title = scanner.nextLine();

            if (DataStore.addInstructor(new Instructor(id, name, email, phone, dept, title))) {
                FileManager.saveData();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }


    private static void polymorphismDemo() {
        System.out.println("\n==== POLYMORPHISM DEMO ====");
        Person s = new Student("S1", "John Doe", "john@gmail.com", "0244000000", "CS", 1);
        Person i = new Instructor("I1", "Dr. Bob", "bob@gimpa.edu.gh", "0244111111", "CS", "Dr.");
        s.displayInfo();
        i.displayInfo();
    }


    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a number: ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }


    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Enter a decimal: ");
            scanner.next();
        }
        double val = scanner.nextDouble();
        scanner.nextLine();
        return val;
    }
}