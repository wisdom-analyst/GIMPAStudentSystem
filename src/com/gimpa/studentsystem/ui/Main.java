package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.*;
import com.gimpa.studentsystem.service.*;
import com.gimpa.studentsystem.exception.EntityNotFoundException; // Import Exception
import com.gimpa.studentsystem.util.FileManager;               // Import FileManager

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // PHASE 5: Load existing data from file on startup
        FileManager.loadAllData();

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   GIMPA STUDENT MANAGEMENT SYSTEM   ║");
        System.out.println("║   PHASE 5: EXCEPTIONS & FILES      ║");
        System.out.println("╚══════════════════════════════════════╝");

        int choice = 0;

        do {
            try {
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
                        // PHASE 5: Save data before closing
                        FileManager.saveAllData();
                        System.out.println("\nData saved. Goodbye!");
                        break;
                    default:
                        System.out.println("\n[ERROR] Invalid choice. Enter 1-15.");
                }
            } catch (Exception e) {
                System.out.println("[CRITICAL ERROR] Something went wrong: " + e.getMessage());
                scanner.nextLine(); // Clear scanner buffer
            }

        } while (choice != 15);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║             MAIN MENU                ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Add Student    | 8. Enroll       ║");
        System.out.println("║  2. Add Course     | 9. Record Grade ║");
        System.out.println("║  3. View Students  | 10. Transcript  ║");
        System.out.println("║  4. View Courses   | 11. Roster      ║");
        System.out.println("║  5. Search Student | 12. Instructor  ║");
        System.out.println("║  6. Update Student | 13. Poly Demo   ║");
        System.out.println("║  7. Delete Student | 14. Summary     ║");
        System.out.println("║  15. SAVE & EXIT                     ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // UPDATED: updateStudent now uses try-catch for Phase 5
    private static void updateStudent() {
        System.out.println("\n===== UPDATE STUDENT =====");
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine();

        try {
            // DataStore now throws EntityNotFoundException instead of returning null
            Student existing = DataStore.getStudentById(studentId);

            System.out.println("Current details:");
            existing.displayInfo();

            System.out.println("Press Enter to keep current value.");
            System.out.print("Name [" + existing.getName() + "]: ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) existing.setName(name);

            System.out.print("Program [" + existing.getProgram() + "]: ");
            String program = scanner.nextLine();
            if (!program.isEmpty()) existing.setProgram(program);

            DataStore.updateStudent(existing);

        } catch (EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // UPDATED: deleteStudent now uses try-catch
    private static void deleteStudent() {
        System.out.println("\n===== DELETE STUDENT =====");
        System.out.print("Enter Student ID to delete: ");
        String studentId = scanner.nextLine();

        try {
            Student student = DataStore.getStudentById(studentId);
            student.displayInfo();

            System.out.print("Are you sure? (Y/N): ");
            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                DataStore.deleteStudent(studentId);
            }
        } catch (EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // UPDATED: viewStudentEnrollments now uses try-catch
    private static void viewStudentEnrollments() {
        System.out.print("\nEnter Student ID: ");
        String id = scanner.nextLine();
        try {
            DataStore.getStudentById(id); // Check if exists
            EnrollmentService.displayTranscript(id);
        } catch (EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // UPDATED: viewCourseRoster now uses try-catch
    private static void viewCourseRoster() {
        System.out.print("\nEnter Course Code: ");
        String code = scanner.nextLine().toUpperCase();
        try {
            Course course = DataStore.getCourseByCode(code);
            System.out.println("\nRoster for: " + course.getCourseTitle());
            List<Student> enrolled = EnrollmentService.getCourseStudents(code);
            for (Student s : enrolled) System.out.println(" - " + s.getName());
        } catch (EntityNotFoundException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    // --- REMAINDER OF YOUR METHODS (addStudent, addCourse, etc.) ---
    // Ensure they call FileManager.saveAllData() if you want instant saving!

    private static void addStudent() {
        System.out.println("\n==== ADD NEW STUDENT ====");
        try {
            System.out.print("ID: "); String id = scanner.nextLine();
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Phone: "); String phone = scanner.nextLine();
            System.out.print("Program: "); String prog = scanner.nextLine();
            int year = getIntInput("Year (1-4): ");

            Student s = new Student(id, name, email, phone, prog, year);
            if (DataStore.addStudent(s)) {
                FileManager.saveAllData(); // Auto-save after adding
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void addCourse() {
        System.out.println("\n==== ADD NEW COURSE ====");
        try {
            System.out.print("Code: "); String code = scanner.nextLine();
            System.out.print("Title: "); String title = scanner.nextLine();
            int creds = getIntInput("Credits: ");
            System.out.print("Instructor: "); String inst = scanner.nextLine();

            if (DataStore.addCourse(new Course(code, title, creds, inst))) {
                FileManager.saveAllData();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void viewAllStudents() {
        ArrayList<Student> students = DataStore.getAllStudents();
        if (students.isEmpty()) System.out.println("No students found.");
        for (Student s : students) s.displayInfo();
    }

    private static void viewAllCourses() {
        ArrayList<Course> courses = DataStore.getAllCourses();
        if (courses.isEmpty()) System.out.println("No courses found.");
        for (Course c : courses) c.displayInfo();
    }

    private static void searchStudent() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        for (Student s : DataStore.searchStudentsByName(name)) s.displayInfo();
    }

    private static void enrollStudent() {
        System.out.print("Student ID: "); String sId = scanner.nextLine();
        System.out.print("Course Code: "); String cCode = scanner.nextLine();
        EnrollmentService.enrollStudent(sId, cCode);
        FileManager.saveAllData();
    }

    private static void recordGrade() {
        System.out.print("Student ID: "); String sId = scanner.nextLine();
        System.out.print("Course Code: "); String cCode = scanner.nextLine();
        double grade = getDoubleInput("Grade: ");
        EnrollmentService.recordGrade(sId, cCode, grade);
        FileManager.saveAllData();
    }

    private static void addInstructor() {
        try {
            System.out.print("ID: "); String id = scanner.nextLine();
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Phone: "); String phone = scanner.nextLine();
            System.out.print("Dept: "); String dept = scanner.nextLine();
            System.out.print("Title: "); String title = scanner.nextLine();

            if (DataStore.addInstructor(new Instructor(id, name, email, phone, dept, title))) {
                FileManager.saveAllData();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void polymorphismDemo() {
        Person s = new Student("S1", "John", "j@g.com", "123", "CS", 1);
        Person i = new Instructor("I1", "Dr. Bob", "b@g.com", "456", "CS", "Dr.");
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