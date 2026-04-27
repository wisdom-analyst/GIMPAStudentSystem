package com.gimpa.studentsystem.service;

import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Enrollment;
import com.gimpa.studentsystem.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnrollmentService {

    // STORAGE -

    // Stores enrollments grouped by studentId , Key = studentId, Value = list of that student's enrollments
    private static HashMap<String, ArrayList<Enrollment>> studentEnrollments
            = new HashMap<>();

    // Stores every enrollment in the system in one flat list
    private static ArrayList<Enrollment> allEnrollments = new ArrayList<>();


    //===ENROLL STUDENT ===
    public static boolean enrollStudent(String studentId, String courseCode) {

        // Step 1 - find the student
        Student student = DataStore.getStudentById(studentId);
        if (student == null) {
            System.out.println("[ERROR] Student not found: " + studentId);
            return false;
        }


        // Step 2 - find the course
        Course course = DataStore.getCourseByCode(courseCode);
        if (course == null) {
            System.out.println("[ERROR] Course not found: " + courseCode);
            return false;
        }

        // Step 3 - check for duplicate enrollment
        if (isEnrolled(studentId, courseCode)) {
            System.out.println("[ERROR] " + student.getName()
                    + " is already enrolled in " + course.getCourseTitle());
            return false;
        }

        // Step 4 - create the enrollment and save it
        Enrollment enrollment = new Enrollment(student, course);
        allEnrollments.add(enrollment);

        // computeIfAbsent: if this student has no list yet, create one first,
        // then add this enrollment to it.
        studentEnrollments
                .computeIfAbsent(studentId, k -> new ArrayList<>())
                .add(enrollment);

        System.out.println("[SUCCESS] " + student.getName()
                + " enrolled in " + course.getCourseTitle());
        return true;
    }

    //== CHECK ENROLLMENT == Returns true if a student is already enrolled in a course
    public static boolean isEnrolled(String studentId, String courseCode) {
        for (Enrollment enrollment : allEnrollments) {
            if (enrollment.getStudent().getStudentId().equals(studentId)
                    && enrollment.getCourse().getCourseCode().equals(courseCode)) {
                return true;
            }
        }
        return false;
    }

    // == RECORD GRADE ==
    public static boolean recordGrade(String studentId,
                                      String courseCode, double grade) {

        // Validate grade range first - reject impossible values immediately
        if (grade < 0 || grade > 100) {
            System.out.println("[ERROR] Grade must be between 0 and 100. Entered: "
                    + grade);
            return false;
        }

        // Find the matching enrollment and update it
        for (Enrollment enrollment : allEnrollments) {
            if (enrollment.getStudent().getStudentId().equals(studentId)
                    && enrollment.getCourse().getCourseCode().equals(courseCode)) {

                enrollment.setGrade(grade); // setGrade() also sets isGraded = true
                System.out.println("[SUCCESS] Grade " + grade + " recorded for "
                        + enrollment.getStudent().getName()
                        + " in " + enrollment.getCourse().getCourseTitle());
                return true;
            }
        }

        // If we reach here, no matching enrollment was found
        System.out.println("[ERROR] No enrollment found for student "
                + studentId + " in course " + courseCode);
        return false;
    }


    //=== RETRIEVE ENROLLMENT ===
    public static ArrayList<Enrollment> getStudentEnrollments(String studentId) {
        // getOrDefault returns the list if found, or a new empty list if not
        return studentEnrollments.getOrDefault(studentId, new ArrayList<>());
    }

    // == Returns all students enrolled in a specific course.
    public static ArrayList<Student> getCourseStudents(String courseCode) {
        ArrayList<Student> students = new ArrayList<>();

        for (Enrollment enrollment : allEnrollments) {
            if (enrollment.getCourse().getCourseCode().equals(courseCode)) {
                students.add(enrollment.getStudent());
            }
        }

        return students;
    }

    //Returns every enrollment in the system - Returns a copy to protect the internal list from outside modification.
    public static ArrayList<Enrollment> getAllEnrollments() {
        return new ArrayList<>(allEnrollments);
    }


    // === GPA CALCULATION ===
    public static double calculateGPA(String studentId) {
        ArrayList<Enrollment> enrollments = getStudentEnrollments(studentId);

        double totalPoints = 0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            if (enrollment.isGraded()) {
                double gradePoint = convertToGradePoint(enrollment.getGrade());
                int credits = enrollment.getCourse().getCredits();

                // Weighted: a 3-credit course counts more than a 1-credit course
                totalPoints += gradePoint * credits;
                totalCredits += credits;
            }
        }

        // Avoid dividing by zero if no graded courses exist
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    // Converts a percentage score to GIMPA's grade point scale.

    public static double convertToGradePoint(double percentage) {
        if (percentage >= 80) return 4.0;
        if (percentage >= 75) return 3.75;
        if (percentage >= 70) return 3.5;
        if (percentage >= 65) return 3.0;
        if (percentage >= 60) return 2.5;
        if (percentage >= 55) return 2.0;
        if (percentage >= 50) return 1.5;
        if (percentage >= 45) return 1.0;
        return 0.0;
    }

    // Converts a percentage score to a letter grade string.

    public static String getLetterGrade(double percentage) {
        if (percentage >= 80) return "A+";
        if (percentage >= 75) return "A";
        if (percentage >= 70) return "B+";
        if (percentage >= 65) return "B";
        if (percentage >= 60) return "C+";
        if (percentage >= 55) return "C";
        if (percentage >= 50) return "D+";
        if (percentage >= 45) return "D";
        return "F";
    }


    // ===== TRANSCRIPT =====

    // Displays a formatted academic transcript for a student.

    public static void displayTranscript(String studentId) {
        Student student = DataStore.getStudentById(studentId);

        if (student == null) {
            System.out.println("[ERROR] Student not found: " + studentId);
            return;
        }

        ArrayList<Enrollment> enrollments = getStudentEnrollments(studentId);

        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║          GIMPA STUDENT TRANSCRIPT            ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.printf( "║  Student : %-33s ║%n", student.getName());
        System.out.printf( "║  ID      : %-33s ║%n", student.getStudentId());
        System.out.printf( "║  Program : %-33s ║%n", student.getProgram());
        System.out.printf( "║  Year    : %-33d ║%n", student.getYearOfStudy());
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  Code       │ Title              │ Cr │ Grade ║");
        System.out.println("╠══════════════════════════════════════════════╣");

        double totalPoints = 0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();
            int credits = course.getCredits();

            String gradeDisplay;
            if (enrollment.isGraded()) {
                gradeDisplay = getLetterGrade(enrollment.getGrade());

                // Accumulate for GPA calculation
                totalPoints += convertToGradePoint(enrollment.getGrade()) * credits;
                totalCredits += credits;
            } else {
                gradeDisplay = "Pending";
            }

            System.out.printf("║  %-11s │ %-18s │ %-2d │ %-6s║%n",
                    course.getCourseCode(),
                    // Truncate long titles so the table stays neat
                    course.getCourseTitle().length() > 18
                            ? course.getCourseTitle().substring(0, 15) + "..."
                            : course.getCourseTitle(),
                    credits,
                    gradeDisplay);
        }

        System.out.println("╠══════════════════════════════════════════════╣");

        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
        System.out.printf( "║  GPA: %-39.2f ║%n", gpa);
        System.out.println("╚══════════════════════════════════════════════╝");
    }

}

