package com.gimpa.studentsystem.test;

import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Enrollment;
import com.gimpa.studentsystem.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * EnrollmentTest — tests all Enrollment class validations.
 * Ensures Enrollment objects behave correctly.
 * JUnit Testing
 */
public class EnrollmentTest {

    // objects used across all tests
    private Student student;
    private Course course;
    private Enrollment enrollment;

    // runs before every test — creates fresh objects
    @BeforeEach
    void setUp() {
        student = new Student(
                "S001",
                "Kwame Mensah",
                "kwame@gimpa.edu.gh",
                "0244123456",
                "Computer Science",
                2
        );

        course = new Course(
                "SOT104B",
                "Object Oriented Programming",
                3,
                "Dr. Mensah"
        );

        enrollment = new Enrollment(student, course);
    }


    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testEnrollmentCreatedSuccessfully() {
        // enrollment should link correct student and course
        assertEquals(student, enrollment.getStudent());
        assertEquals(course, enrollment.getCourse());
    }

    @Test
    void testDefaultGradeIsZero() {
        // grade should start at zero before any grade is recorded
        assertEquals(0.0, enrollment.getGrade());
    }

    @Test
    void testDefaultIsGradedIsFalse() {
        // enrollment should not be graded by default
        assertFalse(enrollment.isGraded());
    }


    // ===== GRADE TESTS =====

    @Test
    void testSetGradeUpdatesGrade() {
        // setting a grade should update the grade value
        enrollment.setGrade(75.5);
        assertEquals(75.5, enrollment.getGrade());
    }

    @Test
    void testSetGradeSetsIsGradedTrue() {
        // setting a grade should mark enrollment as graded
        enrollment.setGrade(80.0);
        assertTrue(enrollment.isGraded());
    }

    @Test
    void testGradeBelowZeroThrowsException() {
        // grade below 0 is invalid
        assertThrows(IllegalArgumentException.class, () -> {
            enrollment.setGrade(-1.0);
        });
    }

    @Test
    void testGradeAbove100ThrowsException() {
        // grade above 100 is invalid
        assertThrows(IllegalArgumentException.class, () -> {
            enrollment.setGrade(101.0);
        });
    }

    @Test
    void testGradeOfZeroIsValid() {
        // grade of exactly 0 is valid
        assertDoesNotThrow(() -> {
            enrollment.setGrade(0.0);
        });
    }

    @Test
    void testGradeOf100IsValid() {
        // grade of exactly 100 is valid
        assertDoesNotThrow(() -> {
            enrollment.setGrade(100.0);
        });
    }


    // ===== NULL TESTS =====

    @Test
    void testNullStudentThrowsException() {
        // null student should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            enrollment.setStudent(null);
        });
    }

    @Test
    void testNullCourseThrowsException() {
        // null course should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            enrollment.setCourse(null);
        });
    }


    // ===== EQUALITY TESTS =====

    @Test
    void testEqualsWithSameStudentAndCourse() {
        // two enrollments with same student and course should be equal
        Enrollment e2 = new Enrollment(student, course);
        assertEquals(enrollment, e2);
    }

    @Test
    void testEqualsWithDifferentCourse() {
        // enrollment with different course should not be equal
        Course differentCourse = new Course(
                "SOT105B", "Data Structures", 3, "Dr. Boateng");
        Enrollment e2 = new Enrollment(student, differentCourse);
        assertNotEquals(enrollment, e2);
    }

    @Test
    void testToStringContainsStudentName() {
        // toString should contain student name
        assertTrue(enrollment.toString().contains("Kwame Mensah"));
    }

    @Test
    void testToStringContainsCourseTitle() {
        // toString should contain course title
        assertTrue(enrollment.toString()
                .contains("Object Oriented Programming"));
    }

    @Test
    void testToStringShowsPendingWhenNotGraded() {
        // toString should show "Not yet graded" when no grade recorded
        assertTrue(enrollment.toString().contains("Not yet graded"));
    }

    @Test
    void testToStringShowsGradeWhenGraded() {
        // toString should show grade value when graded
        enrollment.setGrade(85.0);
        assertTrue(enrollment.toString().contains("85.0"));
    }
}