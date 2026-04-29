package com.gimpa.studentsystem.test;

import com.gimpa.studentsystem.model.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CourseTest — tests all Course class validations.
 * Ensures Course objects behave correctly.
 * JUnit Testing
 */
public class CourseTest {

    // course object used across all tests
    private Course course;

    // runs before every test — creates a fresh course
    @BeforeEach
    void setUp() {
        course = new Course(
                "SOT104B",
                "Object Oriented Programming",
                3,
                "Dr. Mensah"
        );
    }


    // ===== CONSTRUCTOR TESTS =====

    @Test
    void testCourseCreatedSuccessfully() {
        // checks course was created with correct values
        assertEquals("SOT104B", course.getCourseCode());
        assertEquals("Object Oriented Programming", course.getCourseTitle());
        assertEquals(3, course.getCredits());
        assertEquals("Dr. Mensah", course.getInstructor());
    }

    @Test
    void testCourseCodeIsUpperCase() {
        // course code should always be stored in uppercase
        Course c = new Course("sot105b", "Java Programming",
                3, "Dr. Boateng");
        assertEquals("SOT105B", c.getCourseCode());
    }


    // ===== VALIDATION TESTS =====

    @Test
    void testEmptyCourseCodeThrowsException() {
        // empty course code should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            course.setCourseCode("");
        });
    }

    @Test
    void testNullCourseCodeThrowsException() {
        // null course code should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            course.setCourseCode(null);
        });
    }

    @Test
    void testEmptyCourseTitleThrowsException() {
        // empty course title should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            course.setCourseTitle("");
        });
    }

    @Test
    void testNullCourseTitleThrowsException() {
        // null course title should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            course.setCourseTitle(null);
        });
    }

    @Test
    void testZeroCreditsThrowsException() {
        // zero credits is invalid — must be positive
        assertThrows(IllegalArgumentException.class, () -> {
            course.setCredits(0);
        });
    }

    @Test
    void testNegativeCreditsThrowsException() {
        // negative credits should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            course.setCredits(-1);
        });
    }

    @Test
    void testCreditsAboveSixThrowsException() {
        // credits above 6 should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            course.setCredits(7);
        });
    }

    @Test
    void testValidCreditsAccepted() {
        // credits of 3 is valid
        assertDoesNotThrow(() -> {
            course.setCredits(3);
        });
        assertEquals(3, course.getCredits());
    }

    @Test
    void testEmptyInstructorThrowsException() {
        // empty instructor name should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            course.setInstructor("");
        });
    }

    @Test
    void testNullInstructorThrowsException() {
        // null instructor should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            course.setInstructor(null);
        });
    }

    @Test
    void testValidInstructorAccepted() {
        // valid instructor name should be accepted
        assertDoesNotThrow(() -> {
            course.setInstructor("Prof. Adjei");
        });
        assertEquals("Prof. Adjei", course.getInstructor());
    }


    // ===== EQUALITY TESTS =====

    @Test
    void testEqualsWithSameCourseCode() {
        // two courses with same code should be equal
        Course c2 = new Course("SOT104B", "Different Title",
                6, "Different Instructor");
        assertEquals(course, c2);
    }

    @Test
    void testEqualsWithDifferentCourseCode() {
        // two courses with different codes should not be equal
        Course c2 = new Course("SOT999Z", "Different Title",
                3, "Dr. Mensah");
        assertNotEquals(course, c2);
    }

    @Test
    void testToStringContainsCourseCode() {
        // toString should contain the course code
        assertTrue(course.toString().contains("SOT104B"));
    }

    @Test
    void testToStringContainsCourseTitle() {
        // toString should contain the course title
        assertTrue(course.toString().contains("Object Oriented Programming"));
    }
}