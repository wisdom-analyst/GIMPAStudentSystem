package com.gimpa.studentsystem.database;

import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager — handles all SQLite database operations.
 * Creates and manages the database connection.
 * Replaces FileManager for data persistence.
 * JDBC Database Integration
 */
public class DatabaseManager {

    // database file will be created in the project folder
    private static final String DB_URL = "jdbc:sqlite:gimpa_system.db";

    // single shared connection to the database
    private static Connection connection = null;


    // ===== CONNECTION =====

    // opens connection to the SQLite database
    // creates the database file if it doesn't exist yet
    public static void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("[DATABASE] Connected to SQLite database.");
                createTables(); // create tables if they don't exist
            }
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Connection failed: " + e.getMessage());
        }
    }


    // closes the database connection cleanly
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DATABASE] Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Could not close: " + e.getMessage());
        }
    }


    // returns the active connection
    public static Connection getConnection() {
        return connection;
    }


    // ===== CREATE TABLES =====

    // creates all required tables if they don't already exist
    // runs automatically when connecting for the first time
    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {

            // students table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS students (
                        student_id   TEXT PRIMARY KEY,
                        name         TEXT NOT NULL,
                        email        TEXT NOT NULL,
                        phone        TEXT NOT NULL,
                        program      TEXT NOT NULL,
                        year_of_study INTEGER NOT NULL
                    )
                    """);

            // courses table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS courses (
                        course_code  TEXT PRIMARY KEY,
                        course_title TEXT NOT NULL,
                        credits      INTEGER NOT NULL,
                        instructor   TEXT NOT NULL
                    )
                    """);

            // enrollments table
            // references students and courses tables
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS enrollments (
                        id           INTEGER PRIMARY KEY AUTOINCREMENT,
                        student_id   TEXT NOT NULL,
                        course_code  TEXT NOT NULL,
                        grade        REAL DEFAULT 0.0,
                        is_graded    INTEGER DEFAULT 0,
                        FOREIGN KEY (student_id) REFERENCES students(student_id),
                        FOREIGN KEY (course_code) REFERENCES courses(course_code)
                    )
                    """);

            System.out.println("[DATABASE] Tables ready.");

        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Table creation failed: " + e.getMessage());
        }
    }


    // ===== STUDENT OPERATIONS =====

    // saves a student to the database
    public static boolean insertStudent(Student student) {
        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());
            pstmt.setString(5, student.getProgram());
            pstmt.setInt(6, student.getYearOfStudy());
            pstmt.executeUpdate();
            System.out.println("[DATABASE] Student saved: " + student.getName());
            return true;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Insert student failed: " + e.getMessage());
            return false;
        }
    }


    // loads all students from the database
    public static List<Student> loadAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("program"),
                        rs.getInt("year_of_study")
                );
                students.add(s);
            }
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Load students failed: " + e.getMessage());
        }
        return students;
    }


    // updates an existing student in the database
    public static boolean updateStudent(Student student) {
        String sql = """
                UPDATE students SET
                    name = ?, email = ?, phone = ?,
                    program = ?, year_of_study = ?
                WHERE student_id = ?
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getPhone());
            pstmt.setString(4, student.getProgram());
            pstmt.setInt(5, student.getYearOfStudy());
            pstmt.setString(6, student.getStudentId());
            pstmt.executeUpdate();
            System.out.println("[DATABASE] Student updated: " + student.getName());
            return true;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Update student failed: " + e.getMessage());
            return false;
        }
    }


    // deletes a student from the database
    public static boolean deleteStudent(String studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
            System.out.println("[DATABASE] Student deleted: " + studentId);
            return true;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Delete student failed: " + e.getMessage());
            return false;
        }
    }


    // ===== COURSE OPERATIONS =====

    // saves a course to the database
    public static boolean insertCourse(Course course) {
        String sql = "INSERT INTO courses VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseTitle());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getInstructor());
            pstmt.executeUpdate();
            System.out.println("[DATABASE] Course saved: " + course.getCourseTitle());
            return true;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Insert course failed: " + e.getMessage());
            return false;
        }
    }


    // loads all courses from the database
    public static List<Course> loadAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course c = new Course(
                        rs.getString("course_code"),
                        rs.getString("course_title"),
                        rs.getInt("credits"),
                        rs.getString("instructor")
                );
                courses.add(c);
            }
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Load courses failed: " + e.getMessage());
        }
        return courses;
    }


    // updates an existing course in the database
    public static boolean updateCourse(Course course) {
        String sql = """
                UPDATE courses SET
                    course_title = ?, credits = ?, instructor = ?
                WHERE course_code = ?
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseTitle());
            pstmt.setInt(2, course.getCredits());
            pstmt.setString(3, course.getInstructor());
            pstmt.setString(4, course.getCourseCode());
            pstmt.executeUpdate();
            System.out.println("[DATABASE] Course updated: " + course.getCourseTitle());
            return true;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Update course failed: " + e.getMessage());
            return false;
        }
    }


    // deletes a course from the database
    public static boolean deleteCourse(String courseCode) {
        String sql = "DELETE FROM courses WHERE course_code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            pstmt.executeUpdate();
            System.out.println("[DATABASE] Course deleted: " + courseCode);
            return true;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Delete course failed: " + e.getMessage());
            return false;
        }
    }


    // ===== ENROLLMENT OPERATIONS =====

    // saves an enrollment to the database
    public static boolean insertEnrollment(String studentId,
                                           String courseCode) {
        String sql = """
                INSERT INTO enrollments (student_id, course_code)
                VALUES (?, ?)
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseCode);
            pstmt.executeUpdate();
            System.out.println("[DATABASE] Enrollment saved.");
            return true;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Insert enrollment failed: "
                    + e.getMessage());
            return false;
        }
    }


    // updates a grade in the database
    public static boolean updateGrade(String studentId,
                                      String courseCode,
                                      double grade) {
        String sql = """
                UPDATE enrollments SET grade = ?, is_graded = 1
                WHERE student_id = ? AND course_code = ?
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, grade);
            pstmt.setString(2, studentId);
            pstmt.setString(3, courseCode);
            pstmt.executeUpdate();
            System.out.println("[DATABASE] Grade updated.");
            return true;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Update grade failed: "
                    + e.getMessage());
            return false;
        }
    }


    // loads all enrollments as raw data
    // returns list of string arrays [studentId, courseCode, grade, isGraded]
    public static List<String[]> loadAllEnrollments() {
        List<String[]> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments";
        try (Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) {
                enrollments.add(new String[]{
                        rs.getString("student_id"),
                        rs.getString("course_code"),
                        rs.getString("grade"),
                        rs.getString("is_graded")
                });
            }
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Load enrollments failed: "
                    + e.getMessage());
        }
        return enrollments;
    }


    // checks if a student is already enrolled in a course
    public static boolean isEnrolled(String studentId, String courseCode) {
        String sql = """
                SELECT COUNT(*) FROM enrollments
                WHERE student_id = ? AND course_code = ?
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseCode);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("[DATABASE ERROR] Check enrollment failed: "
                    + e.getMessage());
            return false;
        }
    }
}