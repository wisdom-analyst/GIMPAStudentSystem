# GIMPA Student & Course Management System

A Java console application (with a Swing GUI extension) for managing student records, course offerings, enrollments, and grades — built as a semester-long Object-Oriented Programming project at GIMPA.

The system lets an administrator:
- Add, view, update, and delete student records
- Add, view, update, and delete course offerings
- Enroll students in courses, with duplicate-enrollment prevention
- Record and manage grades for enrolled students
- Generate reports, including student transcripts and course rosters
- Persist all data between sessions via file-based storage

## Tech Stack

- **Language:** Java (JDK 11+)
- **GUI:** Java Swing (`JFrame`, `JTabbedPane`, `JTable`)
- **Persistence:** Java serialization (object data saved/loaded from disk between sessions)
- **Testing:** JUnit (71 tests across the Student, Course, and Enrollment modules)
- **Build/IDE:** IntelliJ IDEA

## Project Structure

The system was built in six incremental phases, each layering in a core Java/OOP concept:

| Phase | Focus | What it added |
|-------|-------|---------------|
| 1 | Java Foundations & Project Setup | Initial `Student` / `Course` classes, package structure |
| 2 | Encapsulation & Data Management | Private fields, getters/setters, centralized data store |
| 3 | Inheritance & Polymorphism | Abstract `Person` class; `Student` and `Instructor` as subclasses |
| 4 | Collections & Reporting | `HashMap`-based storage, sorting, filtering, statistics |
| 5 | Exception Handling & File I/O | Custom exceptions, serialization-based save/load |
| 6 | GUI Development with Swing | Full desktop interface — tabs for students, courses, enrollment, and reports |

```
GIMPAStudentSystem/
└── src/
    └── com/gimpa/studentsystem/
        ├── model/       # Person, Student, Instructor, Course
        ├── service/      # DataStore, FileManager, business logic
        ├── exception/    # Custom checked exceptions
        ├── ui/            # Swing GUI components
        └── util/
```

## Running the Project

1. Clone the repo:
   ```bash
   git clone https://github.com/wisdom-analyst/GIMPAStudentSystem.git
   ```
2. Open the project in IntelliJ IDEA (or your preferred IDE) with JDK 11+ configured.
3. Run the `MainFrame` class to launch the application.

## Sample Data

`students_data.txt` and `courses_data.txt` are sample/demo data files included for testing and demonstration purposes — they show the kind of data the system manages, not the live save format (which uses serialization).

## Status

Completed as a 6-phase academic project. All core features implemented and tested (71 passing JUnit tests across Student, Course, and Enrollment functionality).
