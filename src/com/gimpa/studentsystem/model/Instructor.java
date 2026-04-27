package com.gimpa.studentsystem.model;

import java.util.ArrayList;

// Instructor class extends Person - adds instructor-specific attributes.
public class Instructor extends Person implements Manageable {

    // ===== INSTRUCTOR SPECIFIC FIELDS =====
    private String employeeId;
    private String department;
    private String title;                // Dr., Prof., Mr., Ms., Mrs.
    private ArrayList<String> coursesTeaching;

    // ===== CONSTRUCTORS =====
    public Instructor() {
        super();
        this.coursesTeaching = new ArrayList<>();
    }

    public Instructor(String employeeId, String name, String email,
                      String phone, String department, String title) {
        super(name, email, phone);
        setEmployeeId(employeeId);
        setDepartment(department);
        setTitle(title);
        this.coursesTeaching = new ArrayList<>();
    }

    // ===== GETTERS =====
    public String getEmployeeId() { return employeeId; }
    public String getDepartment() { return department; }
    public String getTitle()      { return title;      }
    public ArrayList<String> getCoursesTeaching() {
        return new ArrayList<>(coursesTeaching); // Encapsulation: return a copy
    }

    // ===== SETTERS WITH VALIDATION =====
    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }
        this.employeeId = employeeId.toUpperCase();
    }

    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
        this.department = department.trim();
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        String[] validTitles = {"Dr.", "Prof.", "Mr.", "Ms.", "Mrs."};
        boolean isValid = false;
        for (String t : validTitles) {
            if (t.equals(title)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("Invalid title. Use Dr., Prof., Mr., Ms., or Mrs.");
        }
        this.title = title;
    }

    // ===== COURSE MANAGEMENT =====
    public void assignCourse(String courseCode) {
        if (!coursesTeaching.contains(courseCode)) {
            coursesTeaching.add(courseCode);
            System.out.println("Course " + courseCode + " assigned to " + getName());
        } else {
            System.out.println("Instructor already teaching " + courseCode);
        }
    }

    public void removeCourse(String courseCode) {
        if (coursesTeaching.remove(courseCode)) {
            System.out.println("Course " + courseCode + " removed from " + getName());
        } else {
            System.out.println("Instructor not teaching " + courseCode);
        }
    }

    public String getFullNameWithTitle() {
        return title + " " + getName();
    }

    // ===== IMPLEMENTING ABSTRACT METHODS FROM PERSON =====
    @Override
    public String getRole() {
        return "INSTRUCTOR";
    }

    @Override
    public void displayInfo() {
        displayCommonInfo(); // Calls protected method from Person class
        System.out.println("├─────────────────────────────────────────┤ ");
        System.out.printf("│ Employee ID: %-24s │\n", employeeId);
        System.out.printf("│ Department: %-24s │\n", department);
        System.out.printf("│ Title: %-24s │\n", title);
        System.out.printf("│ Courses: %-24d │\n", coursesTeaching.size());
        if (!coursesTeaching.isEmpty()) {
            System.out.println("│ Teaching:                               │");
            for (String course : coursesTeaching) {
                System.out.printf("│ - %-32s │\n", course);
            }
        }
        System.out.println("└─────────────────────────────────────────┘ ");
    }

    // ===== IMPLEMENTING MANAGEABLE INTERFACE METHODS (Phase 4) =====
    @Override
    public void add() {
        System.out.println("Adding Instructor: " + getFullNameWithTitle());
        // Integration with DataStore happens in the service layer
    }

    @Override
    public void view() {
        displayInfo();
    }

    @Override
    public void update() {
        System.out.println("Updating Instructor: " + getFullNameWithTitle());
    }

    @Override
    public void delete() {
        System.out.println("Deleting Instructor: " + getFullNameWithTitle());
    }

    @Override
    public boolean validate() {
        return employeeId != null && !employeeId.isEmpty() &&
                getName() != null && !getName().isEmpty() &&
                getEmail() != null && getEmail().contains("@") &&
                department != null && !department.isEmpty();
    }

    @Override
    public String getSummary() {
        return String.format("%s: %s (%s)", employeeId, getFullNameWithTitle(), department);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %s",
                getRole(), employeeId, getFullNameWithTitle(), getEmail(), department);
    }
}