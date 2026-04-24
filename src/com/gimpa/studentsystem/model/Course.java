package com.gimpa.studentsystem.model;

// PRIVATE FIELDS - Outsiders must use SETTERS & GETTERS
public class Course {
    private String courseCode;
    private String courseTitle;
    private int credits;
    private String instructor;


    // Default Constrictor - create an empty course object
    public Course() {}

    // Parameterized constructor - create a course with full details, Validation run automatically
    public Course(String courseCode, String courseTitle,
                  int credits, String instructor) {
        setCourseCode(courseCode);
        setCourseTitle(courseTitle);
        setCredits(credits);
        setInstructor(instructor);
    }


    //GETTERS - only way outside code can READ our private fields
    /** Returns the course code e.g. "SOT104B" */
    public String getCourseCode() {
        return courseCode;
    }

    /** Returns the full course title */
    public String getCourseTitle() {
        return courseTitle;
    }

    /** Returns the number of credits for this course */
    public int getCredits() {
        return credits;
    }

    /** Returns the name of the instructor teaching this course */
    public String getInstructor() {
        return instructor;
    }

    //SETTERS - only way outside code can CHANGE our private fields
    public void setCourseCode(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }
        this.courseCode = courseCode.trim().toUpperCase();
    }

    public void setCourseTitle(String courseTitle) {
        if (courseTitle == null || courseTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }
        this.courseTitle = courseTitle.trim();
    }

    public void setCredits(int credits) {
        if (credits < 1) {
            throw new IllegalArgumentException("Credits must be at least 1");
        }
        if (credits > 6) {
            throw new IllegalArgumentException("Credits cannot exceed 6");
        }
        this.credits = credits;
    }

    public void setInstructor(String instructor) {
        if (instructor == null || instructor.trim().isEmpty()) {
            throw new IllegalArgumentException("Instructor name cannot be empty");
        }
        this.instructor = instructor.trim();
    }

    // Print Course Details
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.println("│          COURSE DETAILS             │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.printf("│ Code       : %-22s │%n", courseCode);
        System.out.printf("│ Title      : %-22s │%n", courseTitle);
        System.out.printf("│ Credits    : %-22d │%n", credits);
        System.out.printf("│ Instructor : %-22s │%n", instructor);
        System.out.println("└─────────────────────────────────────┘");
    }

    //UTILITY METHODS - TO STRING
    @Override
    public String toString() {
        return String.format("%s | %s | %d credits | %s",
                courseCode, courseTitle, credits, instructor);
    }

    // EQUALS - to prevent duplicate courses
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // same object in memory
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj; // cast obj to Course so we can use it
        return courseCode != null && courseCode.equals(course.courseCode);
    }

    // HASH code -if two objects are equal, they must have the same hashCode.
    @Override
    public int hashCode() {
        return courseCode != null ? courseCode.hashCode() : 0;
    }

}
