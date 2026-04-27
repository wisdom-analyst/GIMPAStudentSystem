package com.gimpa.studentsystem.model;

import java.util.ArrayList;

    public class Instructor extends Person {

        // ===== INSTRUCTOR SPECIFIC FIELDS =====
        private String employeeId;
        private String department;
        private String title;                        // Dr. Prof. Mr. Ms. Mrs.
        private ArrayList<String> coursesTeaching;   // list of course codes assigned


        // ===== DEFAULT CONSTRUCTOR =====
        public Instructor() {
            super();
            this.coursesTeaching = new ArrayList<>(); // initialize empty list
        }


        // ===== PARAMETERIZED CONSTRUCTOR =====
        public Instructor(String employeeId, String name, String email,
                          String phone, String department, String title) {
            super(name, email, phone); // send common fields up to Person
            setEmployeeId(employeeId);
            setDepartment(department);
            setTitle(title);
            this.coursesTeaching = new ArrayList<>();
        }


        // ===== GETTERS =====
        public String getEmployeeId()              { return employeeId; }
        public String getDepartment()              { return department; }
        public String getTitle()                   { return title; }
        public ArrayList<String> getCoursesTeaching() {
            return new ArrayList<>(coursesTeaching); // return a copy for safety
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
            // only these titles are accepted
            String[] validTitles = {"Dr.", "Prof.", "Mr.", "Ms.", "Mrs."};
            boolean isValid = false;
            for (String t : validTitles) {
                if (t.equals(title)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                throw new IllegalArgumentException("Title must be Dr. Prof. Mr. Ms. or Mrs.");
            }
            this.title = title;
        }


        // ===== COURSE ASSIGNMENT METHODS =====

        // assigns a course to this instructor
        public void assignCourse(String courseCode) {
            if (!coursesTeaching.contains(courseCode)) {
                coursesTeaching.add(courseCode);
                System.out.println("Course " + courseCode + " assigned to " + getName());
            } else {
                System.out.println(getName() + " is already teaching " + courseCode);
            }
        }

        // removes a course from this instructor
        public void removeCourse(String courseCode) {
            if (coursesTeaching.remove(courseCode)) {
                System.out.println("Course " + courseCode + " removed from " + getName());
            } else {
                System.out.println(getName() + " is not teaching " + courseCode);
            }
        }

        // returns full name with title e.g "Dr. Kwame Mensah"
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
            displayCommonInfo(); // prints role, name, email, phone from Person
            System.out.println("Employee ID  : " + employeeId);
            System.out.println("Department   : " + department);
            System.out.println("Title        : " + title);
            System.out.println("Courses      : " + coursesTeaching.size());
            // if teaching any courses, list them
            if (!coursesTeaching.isEmpty()) {
                for (String course : coursesTeaching) {
                    System.out.println("             - " + course);
                }
            }
            System.out.println("========================================");
        }

        // ===== toString =====
        @Override
        public String toString() {
            return employeeId + " | " + getFullNameWithTitle() + " | " + getDepartment();
        }
    }

