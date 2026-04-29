package com.gimpa.studentsystem.test;

import com.gimpa.studentsystem.database.DatabaseManager;
import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.service.DataStore;
import com.gimpa.studentsystem.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;

/**
 * DataStoreTest — tests all DataStore operations.
 * Ensures data is stored, retrieved and deleted correctly.
 * JUnit Testing
 */
public class DataStoreTest {

    private Student student;
    private Course course;


    // runs before every test
    @BeforeEach
    void setUp() {
        // connect to database before each test
        DatabaseManager.connect();

        student = new Student(
                "TEST001",
                "Test Student",
                "test@gimpa.edu.gh",
                "0244000000",
                "Computer Science",
                1
        );

        course = new Course(
                "TEST101",
                "Test Course",
                3,
                "Dr. Test"
        );

        // clean up before each test
        DataStore.deleteStudent("TEST001");
        DataStore.deleteCourse("TEST101");
    }


    // closes database connection after all tests are done
    @AfterAll
    static void tearDown() {
        DatabaseManager.disconnect();
    }


    // ===== STUDENT TESTS =====

    @Test
    void testAddStudentSuccessfully() {
        assertTrue(DataStore.addStudent(student));
    }

    @Test
    void testAddDuplicateStudentReturnsFalse() {
        DataStore.addStudent(student);
        assertFalse(DataStore.addStudent(student));
    }

    @Test
    void testGetStudentByIdReturnsCorrectStudent()
            throws EntityNotFoundException {
        DataStore.addStudent(student);
        Student found = DataStore.getStudentById("TEST001");
        assertEquals("TEST001", found.getStudentId());
        assertEquals("Test Student", found.getName());
    }

    @Test
    void testGetStudentByIdThrowsWhenNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            DataStore.getStudentById("NOTEXIST999");
        });
    }

    @Test
    void testFindStudentByIdReturnsNullWhenNotFound() {
        assertNull(DataStore.findStudentById("NOTEXIST999"));
    }

    @Test
    void testDeleteStudentSuccessfully() {
        DataStore.addStudent(student);
        assertTrue(DataStore.deleteStudent("TEST001"));
    }

    @Test
    void testDeleteNonExistentStudentReturnsFalse() {
        assertFalse(DataStore.deleteStudent("NOTEXIST999"));
    }

    @Test
    void testUpdateStudentSuccessfully() {
        DataStore.addStudent(student);
        student.setName("Updated Name");
        assertTrue(DataStore.updateStudent(student));
    }

    @Test
    void testStudentCountIncreasesAfterAdd() {
        int countBefore = DataStore.getStudentCount();
        DataStore.addStudent(student);
        assertEquals(countBefore + 1, DataStore.getStudentCount());
    }

    @Test
    void testStudentCountDecreasesAfterDelete() {
        DataStore.addStudent(student);
        int countAfterAdd = DataStore.getStudentCount();
        DataStore.deleteStudent("TEST001");
        assertEquals(countAfterAdd - 1, DataStore.getStudentCount());
    }

    @Test
    void testSearchStudentsByNameReturnsResults() {
        DataStore.addStudent(student);
        assertFalse(DataStore.searchStudentsByName("Test").isEmpty());
    }

    @Test
    void testSearchStudentsByNameReturnsEmptyWhenNoMatch() {
        assertTrue(DataStore.searchStudentsByName(
                "ZZZNOMATCH999").isEmpty());
    }


    // ===== COURSE TESTS =====

    @Test
    void testAddCourseSuccessfully() {
        assertTrue(DataStore.addCourse(course));
    }

    @Test
    void testAddDuplicateCourseReturnsFalse() {
        DataStore.addCourse(course);
        assertFalse(DataStore.addCourse(course));
    }

    @Test
    void testGetCourseByCodeReturnsCorrectCourse()
            throws EntityNotFoundException {
        DataStore.addCourse(course);
        Course found = DataStore.getCourseByCode("TEST101");
        assertEquals("TEST101", found.getCourseCode());
        assertEquals("Test Course", found.getCourseTitle());
    }

    @Test
    void testGetCourseByCodeThrowsWhenNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            DataStore.getCourseByCode("NOTEXIST999");
        });
    }

    @Test
    void testFindCourseByCodeReturnsNullWhenNotFound() {
        assertNull(DataStore.findCourseByCode("NOTEXIST999"));
    }

    @Test
    void testDeleteCourseSuccessfully() {
        DataStore.addCourse(course);
        assertTrue(DataStore.deleteCourse("TEST101"));
    }

    @Test
    void testCourseCountIncreasesAfterAdd() {
        int countBefore = DataStore.getCourseCount();
        DataStore.addCourse(course);
        assertEquals(countBefore + 1, DataStore.getCourseCount());
    }

    @Test
    void testCourseCountDecreasesAfterDelete() {
        DataStore.addCourse(course);
        int countAfterAdd = DataStore.getCourseCount();
        DataStore.deleteCourse("TEST101");
        assertEquals(countAfterAdd - 1, DataStore.getCourseCount());
    }
}