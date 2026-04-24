package com.gimpa.studentsystem.service;
import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Student;

import java.util.ArrayList;
import java.util.HashMap;


public class DataStore {
    // STORAGE
    private static final HashMap<String, Student> studentMap =  new HashMap<>();
    private static final HashMap<String, Course> courseMap = new HashMap<>();

    // PRIVATE CONSTRUCTOR
    private DataStore() {
    }

    //STUDENT OPERATION - add new student to the system
    public static boolean addStudent(Student student) {
        String studentId = student.getStudentId();
        // containsKey() checks if this ID is already in the map
        if (studentMap.containsKey(studentId)) {
            System.out.println("[ERROR] Student with ID "
                    + studentId + " already exists.");
            return false;
        }
        // put() stores the student using their ID as the key
        studentMap.put(studentId, student);
        System.out.println("[SUCCESS] Student added: " + student.getName());
        return true;
    }

    // Finds and returns a single student by their ID.
    public static Student getStudentById(String studentId) {
        return studentMap.get(studentId);
    }

    // Returns a list of ALL students currently in the system.
    public static ArrayList<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    //Updates an existing student's information
    public static boolean updateStudent(Student student) {
        String studentId = student.getStudentId();

        if (!studentMap.containsKey(studentId)) {
            System.out.println("[ERROR] Student with ID "
                    + studentId + " not found.");
            return false;
        }

        // put() with an existing key replaces the old value
        studentMap.put(studentId, student);
        System.out.println("[SUCCESS] Student updated: " + student.getName());
        return true;
    }

    // Removes a student from the system permanently
    public static boolean deleteStudent(String studentId) {
        Student removed = studentMap.remove(studentId);

        if (removed != null) {
            System.out.println("[SUCCESS] Student deleted: " + removed.getName());
            return true;
        }

        System.out.println("[ERROR] Student with ID " + studentId + " not found.");
        return false;
    }

    //Searches for students whose name contains the search text - Returns a list of all matching students.
    public static ArrayList<Student> searchStudentsByName(String nameSearch) {
        ArrayList<Student> results = new ArrayList<>();

        // Convert search term to lowercase for case-insensitive comparison
        String searchLower = nameSearch.toLowerCase();

        // Loop through every student and check if their name contains the search term
        for (Student student : studentMap.values()) {
            if (student.getName().toLowerCase().contains(searchLower)) {
                results.add(student);
            }
        }
        return results;
    }

    // Returns the total number of students in the system
    public static int getStudentCount() {
        return studentMap.size();
    }

    // COURSE OPERATION - add a new course to the system
    public static boolean addCourse(Course course) {
        String courseCode = course.getCourseCode();

        if (courseMap.containsKey(courseCode)) {
            System.out.println("[ERROR] Course with code "
                    + courseCode + " already exists.");
            return false;
        }

        courseMap.put(courseCode, course);
        System.out.println("[SUCCESS] Course added: " + course.getCourseTitle());
        return true;
    }

    // Finds and returns a single course by its code
    public static Course getCourseByCode(String courseCode) {
        return courseMap.get(courseCode);
    }

    // Returns a list of ALL courses currently in the system
    public static ArrayList<Course> getAllCourses() {
        return new ArrayList<>(courseMap.values());
    }

    // Updates an existing course's information - returns true if updated, false if course not found
    public static boolean updateCourse(Course course) {
        String courseCode = course.getCourseCode();

        if (!courseMap.containsKey(courseCode)) {
            System.out.println("[ERROR] Course with code "
                    + courseCode + " not found.");
            return false;
        }

        courseMap.put(courseCode, course);
        System.out.println("[SUCCESS] Course updated: " + course.getCourseTitle());
        return true;
    }

    // Removes a course from the system permanently - returns true if deleted, false if course not found
    public static boolean deleteCourse(String courseCode) {
        Course removed = courseMap.remove(courseCode);

        if (removed != null) {
            System.out.println("[SUCCESS] Course deleted: " + removed.getCourseTitle());
            return true;
        }

        System.out.println("[ERROR] Course with code " + courseCode + " not found.");
        return false;
    }

    //Returns the total number of courses in the system.

    public static int getCourseCount() {
        return courseMap.size();
    }

    // SYSTEM SUMMARY - Prints a quick overview of how much data is in the system
    public static void displaySummary() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          SYSTEM SUMMARY              ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf( "║  Total Students : %-18d ║%n", studentMap.size());
        System.out.printf( "║  Total Courses  : %-18d ║%n", courseMap.size());
        System.out.println("╚══════════════════════════════════════╝");
    }

    // Returns true if BOTH student and course maps are empty - to check if there is any data loaded yet
    public static boolean isEmpty() {
        return studentMap.isEmpty() && courseMap.isEmpty();
    }
}
