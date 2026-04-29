package com.gimpa.studentsystem.test;

import com.gimpa.studentsystem.model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * StudentTest — tests all Student class validations.
 * Ensures Student objects behave correctly.
 * JUnit Testing
 */
public class StudentTest {

    // student object used across all tests
    private Student student;

    // runs before EVERY test method
    // creates a fresh student so tests don't affect each other
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
    }


    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testStudentCreatedSuccessfully() {
        // checks student was created with correct values
        assertEquals("S001", student.getStudentId());
        assertEquals("Kwame Mensah", student.getName());
        assertEquals("kwame@gimpa.edu.gh", student.getEmail());
        assertEquals("0244123456", student.getPhone());
        assertEquals("Computer Science", student.getProgram());
        assertEquals(2, student.getYearOfStudy());
    }

    @Test
    void testStudentIdIsUpperCase() {
        // student ID should always be stored in uppercase
        Student s = new Student("s002", "Ama", "ama@gmail.com",
                "0244000000", "IT", 1);
        assertEquals("S002", s.getStudentId());
    }


    // ===== VALIDATION TESTS =====

    @Test
    void testEmptyStudentIdThrowsException() {
        // empty student ID should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            student.setStudentId("");
        });
    }

    @Test
    void testNullStudentIdThrowsException() {
        // null student ID should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            student.setStudentId(null);
        });
    }

    @Test
    void testInvalidEmailThrowsException() {
        // email without @ should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            student.setEmail("invalidemail");
        });
    }

    @Test
    void testValidEmailAccepted() {
        // valid email should be accepted without exception
        assertDoesNotThrow(() -> {
            student.setEmail("test@example.com");
        });
    }

    @Test
    void testYearBelowRangeThrowsException() {
        // year 0 is invalid — must be between 1 and 4
        assertThrows(IllegalArgumentException.class, () -> {
            student.setYearOfStudy(0);
        });
    }

    @Test
    void testYearAboveRangeThrowsException() {
        // year 5 is invalid — must be between 1 and 4
        assertThrows(IllegalArgumentException.class, () -> {
            student.setYearOfStudy(5);
        });
    }

    @Test
    void testValidYearAccepted() {
        // year 3 is valid
        assertDoesNotThrow(() -> {
            student.setYearOfStudy(3);
        });
        assertEquals(3, student.getYearOfStudy());
    }

    @Test
    void testEmptyNameThrowsException() {
        // empty name should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            student.setName("");
        });
    }


    // ===== BEHAVIOR TESTS =====

    @Test
    void testPromoteIncreasesYear() {
        // promote should increase year by 1
        int yearBefore = student.getYearOfStudy();
        student.promote();
        assertEquals(yearBefore + 1, student.getYearOfStudy());
    }

    @Test
    void testPromoteDoesNotExceedFinalYear() {
        // student in year 4 cannot be promoted further
        student.setYearOfStudy(4);
        student.promote();
        assertEquals(4, student.getYearOfStudy());
    }

    @Test
    void testEligibleForGraduationInYear4() {
        // only year 4 students are eligible for graduation
        student.setYearOfStudy(4);
        assertTrue(student.isEligibleForGraduation());
    }

    @Test
    void testNotEligibleForGraduationBeforeYear4() {
        // year 2 student is not eligible for graduation
        student.setYearOfStudy(2);
        assertFalse(student.isEligibleForGraduation());
    }

    @Test
    void testEqualsWithSameId() {
        // two students with same ID should be equal
        Student s2 = new Student("S001", "Different Name",
                "other@gmail.com", "0200000000", "IT", 1);
        assertEquals(student, s2);
    }

    @Test
    void testEqualsWithDifferentId() {
        // two students with different IDs should not be equal
        Student s2 = new Student("S999", "Different Name",
                "other@gmail.com", "0200000000", "IT", 1);
        assertNotEquals(student, s2);
    }

    @Test
    void testToStringContainsStudentId() {
        // toString should contain the student ID
        assertTrue(student.toString().contains("S001"));
    }
}