package com.gimpa.studentsystem.service;

import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.model.Instructor;
import com.gimpa.studentsystem.exception.EntityNotFoundException;
import com.gimpa.studentsystem.database.DatabaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DataStore — centralized data management using HashMaps.
 * Now integrated with SQLite database for persistent storage.
 * JDBC Database Integration
 */
public class DataStore {

    // ===== STORAGE =====
    // HashMaps keep data in memory for fast access during runtime
    // Database keeps data saved permanently on disk
    private static final HashMap<String, Student>    studentMap    = new HashMap<>();
    private static final HashMap<String, Course>     courseMap     = new HashMap<>();
    private static final HashMap<String, Instructor> instructorMap = new HashMap<>();


    // private constructor — prevents creating DataStore objects
    private DataStore() {}


    // ===== DATABASE INITIALIZATION =====

    // connects to database and loads all saved data into memory
    // called once when the program starts
    public static void initializeFromDatabase() {
        DatabaseManager.connect();

        // load all students from database into HashMap
        List<Student> students = DatabaseManager.loadAllStudents();
        for (Student s : students) {
            studentMap.put(s.getStudentId(), s);
        }

        // load all courses from database into HashMap
        List<Course> courses = DatabaseManager.loadAllCourses();
        for (Course c : courses) {
            courseMap.put(c.getCourseCode(), c);
        }

        // load enrollments AFTER students and courses are ready
        EnrollmentService.loadEnrollmentsFromDatabase();

        System.out.println("[DATABASE] Data loaded into memory successfully.");
    }


    // ===== STUDENT OPERATIONS =====

    // adds student to both HashMap and database
    public static boolean addStudent(Student student) {
        String studentId = student.getStudentId();
        if (studentMap.containsKey(studentId)) {
            System.out.println("[ERROR] Student with ID " + studentId + " already exists.");
            return false;
        }
        studentMap.put(studentId, student);
        DatabaseManager.insertStudent(student); // save to database
        System.out.println("[SUCCESS] Student added: " + student.getName());
        return true;
    }


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


    // updates student in both HashMap and database
    public static boolean updateStudent(Student student) {
        String studentId = student.getStudentId();
        if (!studentMap.containsKey(studentId)) {
            System.out.println("[ERROR] Student with ID " + studentId + " not found.");
            return false;
        }
        studentMap.put(studentId, student);
        DatabaseManager.updateStudent(student); // update in database
        System.out.println("[SUCCESS] Student updated: " + student.getName());
        return true;
    }


    // deletes student from both HashMap and database
    public static boolean deleteStudent(String studentId) {
        Student removed = studentMap.remove(studentId);
        if (removed != null) {
            DatabaseManager.deleteStudent(studentId); // delete from database
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


    // returns null instead of throwing exception — safe for GUI use
    public static Student findStudentById(String studentId) {
        return studentMap.get(studentId);
    }


    // ===== COURSE OPERATIONS =====

    // adds course to both HashMap and database
    public static boolean addCourse(Course course) {
        String courseCode = course.getCourseCode();
        if (courseMap.containsKey(courseCode)) {
            System.out.println("[ERROR] Course with code " + courseCode + " already exists.");
            return false;
        }
        courseMap.put(courseCode, course);
        DatabaseManager.insertCourse(course); // save to database
        System.out.println("[SUCCESS] Course added: " + course.getCourseTitle());
        return true;
    }


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


    // updates course in both HashMap and database
    public static boolean updateCourse(Course course) {
        String courseCode = course.getCourseCode();
        if (!courseMap.containsKey(courseCode)) {
            System.out.println("[ERROR] Course with code " + courseCode + " not found.");
            return false;
        }
        courseMap.put(courseCode, course);
        DatabaseManager.updateCourse(course); // update in database
        System.out.println("[SUCCESS] Course updated: " + course.getCourseTitle());
        return true;
    }


    // deletes course from both HashMap and database
    public static boolean deleteCourse(String courseCode) {
        Course removed = courseMap.remove(courseCode);
        if (removed != null) {
            DatabaseManager.deleteCourse(courseCode); // delete from database
            System.out.println("[SUCCESS] Course deleted: " + removed.getCourseTitle());
            return true;
        }
        System.out.println("[ERROR] Course with code " + courseCode + " not found.");
        return false;
    }


    public static int getCourseCount() {
        return courseMap.size();
    }


    // returns null instead of throwing exception — safe for GUI use
    public static Course findCourseByCode(String courseCode) {
        return courseMap.get(courseCode);
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