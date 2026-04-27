package com.gimpa.studentsystem.service;

import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.model.Instructor;
import com.gimpa.studentsystem.exception.EntityNotFoundException; // Import custom exception

import java.util.ArrayList;
import java.util.HashMap;

public class DataStore {
    // STORAGE
    private static final HashMap<String, Student> studentMap = new HashMap<>();
    private static final HashMap<String, Course> courseMap = new HashMap<>();
    private static final HashMap<String, Instructor> instructorMap = new HashMap<>();

    // PRIVATE CONSTRUCTOR
    private DataStore() {
    }

    // ===== STUDENT OPERATIONS =====

    public static boolean addStudent(Student student) {
        String studentId = student.getStudentId();
        if (studentMap.containsKey(studentId)) {
            System.out.println("[ERROR] Student with ID " + studentId + " already exists.");
            return false;
        }
        studentMap.put(studentId, student);
        System.out.println("[SUCCESS] Student added: " + student.getName());
        return true;
    }

    // Phase 5 Update: Added 'throws EntityNotFoundException'
    public static Student getStudentById(String studentId) throws EntityNotFoundException {
        Student student = studentMap.get(studentId);
        if (student == null) {
            throw new EntityNotFoundException("Student", studentId);
        }
        return student;
    }

    public static ArrayList<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    public static boolean updateStudent(Student student) {
        String studentId = student.getStudentId();
        if (!studentMap.containsKey(studentId)) {
            System.out.println("[ERROR] Student with ID " + studentId + " not found.");
            return false;
        }
        studentMap.put(studentId, student);
        System.out.println("[SUCCESS] Student updated: " + student.getName());
        return true;
    }

    public static boolean deleteStudent(String studentId) {
        Student removed = studentMap.remove(studentId);
        if (removed != null) {
            System.out.println("[SUCCESS] Student deleted: " + removed.getName());
            return true;
        }
        System.out.println("[ERROR] Student with ID " + studentId + " not found.");
        return false;
    }

    public static ArrayList<Student> searchStudentsByName(String nameSearch) {
        ArrayList<Student> results = new ArrayList<>();
        String searchLower = nameSearch.toLowerCase();
        for (Student student : studentMap.values()) {
            if (student.getName().toLowerCase().contains(searchLower)) {
                results.add(student);
            }
        }
        return results;
    }

    public static int getStudentCount() {
        return studentMap.size();
    }

    // ===== COURSE OPERATIONS =====

    public static boolean addCourse(Course course) {
        String courseCode = course.getCourseCode();
        if (courseMap.containsKey(courseCode)) {
            System.out.println("[ERROR] Course with code " + courseCode + " already exists.");
            return false;
        }
        courseMap.put(courseCode, course);
        System.out.println("[SUCCESS] Course added: " + course.getCourseTitle());
        return true;
    }

    // Phase 5 Update: Added 'throws EntityNotFoundException'
    public static Course getCourseByCode(String courseCode) throws EntityNotFoundException {
        Course course = courseMap.get(courseCode);
        if (course == null) {
            throw new EntityNotFoundException("Course", courseCode);
        }
        return course;
    }

    public static ArrayList<Course> getAllCourses() {
        return new ArrayList<>(courseMap.values());
    }

    public static boolean updateCourse(Course course) {
        String courseCode = course.getCourseCode();
        if (!courseMap.containsKey(courseCode)) {
            System.out.println("[ERROR] Course with code " + courseCode + " not found.");
            return false;
        }
        courseMap.put(courseCode, course);
        System.out.println("[SUCCESS] Course updated: " + course.getCourseTitle());
        return true;
    }

    public static boolean deleteCourse(String courseCode) {
        Course removed = courseMap.remove(courseCode);
        if (removed != null) {
            System.out.println("[SUCCESS] Course deleted: " + removed.getCourseTitle());
            return true;
        }
        System.out.println("[ERROR] Course with code " + courseCode + " not found.");
        return false;
    }

    public static int getCourseCount() {
        return courseMap.size();
    }

    // ===== INSTRUCTOR OPERATIONS =====

    public static boolean addInstructor(Instructor instructor) {
        String id = instructor.getEmployeeId();
        if (instructorMap.containsKey(id)) {
            System.out.println("[ERROR] Instructor ID " + id + " already exists.");
            return false;
        }
        instructorMap.put(id, instructor);
        System.out.println("[SUCCESS] Instructor added: " + instructor.getName());
        return true;
    }

    // Phase 5 Update: Added 'throws EntityNotFoundException'
    public static Instructor getInstructorById(String id) throws EntityNotFoundException {
        Instructor instructor = instructorMap.get(id);
        if (instructor == null) {
            throw new EntityNotFoundException("Instructor", id);
        }
        return instructor;
    }

    public static ArrayList<Instructor> getAllInstructors() {
        return new ArrayList<>(instructorMap.values());
    }

    public static int getInstructorCount() {
        return instructorMap.size();
    }

    // ===== SYSTEM SUMMARY =====

    public static void displaySummary() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          SYSTEM SUMMARY              ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║  Total Students    : %-15d ║%n", studentMap.size());
        System.out.printf("║  Total Courses     : %-15d ║%n", courseMap.size());
        System.out.printf("║  Total Instructors : %-15d ║%n", instructorMap.size());
        System.out.println("╚══════════════════════════════════════╝");
    }

    public static boolean isEmpty() {
        return studentMap.isEmpty() && courseMap.isEmpty() && instructorMap.isEmpty();
    }
}