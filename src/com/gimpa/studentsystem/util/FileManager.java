package com.gimpa.studentsystem.util;

import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.service.DataStore;
import com.gimpa.studentsystem.service.EnrollmentService;

import java.io.*;

/**
 * FileManager — handles all file operations.
 * Saves, loads and exports system data.
 * Phase 5 - File I/O
 */
public class FileManager {

    private static final String STUDENT_FILE    = "students_data.txt";
    private static final String COURSE_FILE     = "courses_data.txt";

    // ===== SAVE =====

    // saves all students and courses to files
    public static void saveData() {
        saveStudents();
        saveCourses();
        System.out.println("[FILE] All data saved successfully.");
    }

    // saves all students to file
    private static void saveStudents() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (Student s : DataStore.getAllStudents()) {
                // format: ID|Name|Email|Phone|Program|Year
                writer.println(s.getStudentId() + "|" +
                        s.getName()        + "|" +
                        s.getEmail()       + "|" +
                        s.getPhone()       + "|" +
                        s.getProgram()     + "|" +
                        s.getYearOfStudy());
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save students: " + e.getMessage());
        }
    }

    // saves all courses to file
    private static void saveCourses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSE_FILE))) {
            for (Course c : DataStore.getAllCourses()) {
                // format: Code|Title|Credits|Instructor
                writer.println(c.getCourseCode()  + "|" +
                        c.getCourseTitle() + "|" +
                        c.getCredits()     + "|" +
                        c.getInstructor());
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save courses: " + e.getMessage());
        }
    }


    // ===== LOAD =====

    // loads all students and courses from files
    public static void loadData() {
        loadStudents();
        loadCourses();
        System.out.println("[FILE] All data loaded successfully.");
    }

    // loads students from file
    private static void loadStudents() {
        File file = new File(STUDENT_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 6) {
                    Student s = new Student(p[0], p[1], p[2], p[3], p[4],
                            Integer.parseInt(p[5]));
                    DataStore.addStudent(s);
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Could not load students: " + e.getMessage());
        }
    }

    // loads courses from file
    private static void loadCourses() {
        File file = new File(COURSE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 4) {
                    Course c = new Course(p[0], p[1],
                            Integer.parseInt(p[2]), p[3]);
                    DataStore.addCourse(c);
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Could not load courses: " + e.getMessage());
        }
    }


    // ===== EXPORT TO CSV =====

    // exports data to CSV based on type — "students", "courses", "enrollments"
    public static void exportToCSV(String filename, String type) throws IOException {
        switch (type.toLowerCase()) {
            case "students":
                exportStudentsCSV(filename);
                break;
            case "courses":
                exportCoursesCSV(filename);
                break;
            case "enrollments":
                exportEnrollmentsCSV(filename);
                break;
            default:
                throw new IllegalArgumentException("Unknown export type: " + type);
        }
    }

    // exports students to CSV
    private static void exportStudentsCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Student ID,Name,Email,Phone,Program,Year");
            for (Student s : DataStore.getAllStudents()) {
                writer.printf("%s,%s,%s,%s,%s,%d%n",
                        s.getStudentId(), s.getName(), s.getEmail(),
                        s.getPhone(), s.getProgram(), s.getYearOfStudy());
            }
            System.out.println("[FILE] Students exported to " + filename);
        }
    }

    // exports courses to CSV
    private static void exportCoursesCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Course Code,Title,Credits,Instructor");
            for (Course c : DataStore.getAllCourses()) {
                writer.printf("%s,%s,%d,%s%n",
                        c.getCourseCode(), c.getCourseTitle(),
                        c.getCredits(), c.getInstructor());
            }
            System.out.println("[FILE] Courses exported to " + filename);
        }
    }

    // exports enrollments to CSV
    private static void exportEnrollmentsCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Student ID,Student Name,Course Code,Course Title,Grade,Status");
            EnrollmentService.getAllEnrollments().forEach(e -> {
                String grade  = e.isGraded() ? String.valueOf(e.getGrade()) : "Pending";
                String status = e.isGraded() ? "Completed" : "Enrolled";
                writer.printf("%s,%s,%s,%s,%s,%s%n",
                        e.getStudent().getStudentId(),
                        e.getStudent().getName(),
                        e.getCourse().getCourseCode(),
                        e.getCourse().getCourseTitle(),
                        grade, status);
            });
            System.out.println("[FILE] Enrollments exported to " + filename);
        }
    }

    // checks if data files exist
    public static boolean dataExists() {
        return new File(STUDENT_FILE).exists();
    }
}