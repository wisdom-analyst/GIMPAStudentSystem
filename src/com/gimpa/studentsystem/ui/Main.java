package com.gimpa.studentsystem.ui;

//import Arraylist
import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Student;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the Student Management System.
 * Handles menu display and user interactions.
 */

public class Main {

    // input reader
    private static final Scanner scanner = new Scanner(System.in);

    // Data Storage in Arraylist
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final ArrayList<Course> courses = new ArrayList<>();

    // Program entry point
    static void main() {
        System.out.println("========================================");
        System.out.println("   GIMPA STUDENT MANAGEMENT SYSTEM     ");
        System.out.println("   OBJECT ORIENTED PROGRAMMING 1       ");
        System.out.println("========================================");
        System.out.println("   Welcome! Please select an option.   ");

        int choice;

        // Main Loop
        do {
            displayMenu();  // show the menu options
            choice = getIntInput ("Enter your choice: ");

            //Switch checks the value of choice and runs matching case
            switch (choice)  {
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
                                    //exit message before program closes
                                    System.out.println("\nThank you for using GIMPA Student Management System");
                                    break;

                                    default:
                                        // runs if user type anything other than 1-5
                                        System.out.println("\n[ERROR] Invalid choice. Enter a number between 1-5");
            }
        } while (choice != 5);

        scanner.close();
    }
//=== MENU ===
    private static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("              MAIN MENU                 ");
        System.out.println("========================================");
        System.out.println("  1. Add New Student");
        System.out.println("  2. Add New Course");
        System.out.println("  3. View All Students");
        System.out.println("  4. View All Courses");
        System.out.println("  5. Exit");
        System.out.println("========================================");
    }
// ====== ADD STUDENT =======
    private static void addStudent() {
        System.out.println("\n==== ADD NEW STUDENT ====");

// Prompt user and read every piece of information
        System.out.print("Enter Student ID: ");
        String studentID = scanner.nextLine();

        System.out.print("Enter Student Name: ");
        String studentName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();

// Create a new student with the information collected
        Student student = new Student(studentID, studentName, email, phone);

// Add the new student to the students list
        students.add(student);

        System.out.println("\n[SUCCESS] Student Added Successfully! Total Students: "  + students.size());
    }

// ADD COURSE - Collect course details from user and adds to the list
    private static void addCourse() {
        System.out.println("\n==== ADD NEW COURSE ====");

        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();

        System.out.print("Enter Course Title: ");
        String courseTitle = scanner.nextLine();

// getIntInput handles number input without crashing
        int credits  = getIntInput ("Enter Credits: ");

        System.out.println("Enter Instructor: ");
        String instructor = scanner.nextLine();

// Create a new course object and add to courses list
        Course course = new Course(courseCode, courseTitle, credits, instructor);
        courses.add(course);

        System.out.println("/n[SUCCESS] Course Added Successfully! Total Courses: "  + courses.size());
    }


// VIEW ALL STUDENTS
private static void viewAllStudents() {
        System.out.println("\n=====ALL STUDENTS=====");

// Check if List is empty before trying to display
if (students.isEmpty()) {
    System.out.println("No Student Found, Add Student First");
    return;}

    System.out.println("Total Students: " + students.size());

// Loop through the students list and call their displayInfo() method
        for  (Student student : students) {
            student.displayInfo();
        }
    }

 // VIEW ALL COURSES - Loop through the courses list and display each one
 private static void viewAllCourses() {
        System.out.println("\n=====ALL COURSES=====");

        if (courses.isEmpty()) {
            System.out.println("No Course Found, Add Courses First");
            return;
        }

        System.out.println("Total Courses: " + courses.size());

        for  (Course course : courses) {
            course.displayInfo();
        }
 }

 // INT INPUT - Program react to text inout instead of number
    private static int getIntInput (String prompt) {
        System.out.print(prompt);

 // Keep asking till user type a valid number
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid Input. Enter a Number: ");
            scanner.next();
        }

        int input  = scanner.nextInt();
        scanner.nextLine();
        return input;       // send valid number to whoever called it
    }
}
