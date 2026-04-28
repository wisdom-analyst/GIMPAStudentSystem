package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.Enrollment;
import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.service.DataStore;
import com.gimpa.studentsystem.service.EnrollmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * ReportPanel — generates and displays system reports in the GUI.
 * Phase 6 - GUI Development
 */
public class ReportPanel extends JPanel {

    private final MainFrame mainFrame;
    private JTextArea reportArea;
    private JComboBox<String> reportTypeCombo;
    private JTextField inputField;
    private JLabel inputLabel;


    public ReportPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(MainFrame.COLOR_BG);
        setLayout(new BorderLayout(0, 0));
        buildToolbar();
        buildReportArea();
    }


    // ===== TOOLBAR =====
    private void buildToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(MainFrame.COLOR_PANEL);
        toolbar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, MainFrame.COLOR_BORDER));

        // report type dropdown
        JLabel typeLabel = new JLabel("Report Type:");
        typeLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        typeLabel.setFont(MainFrame.FONT_REGULAR);

        String[] reportTypes = {
                "Student Transcript",
                "Course Roster",
                "System Summary",
                "Dean's List",
                "All Enrollments"
        };

        reportTypeCombo = new JComboBox<>(reportTypes);
        reportTypeCombo.setBackground(MainFrame.COLOR_BG);
        reportTypeCombo.setForeground(MainFrame.COLOR_TEXT);
        reportTypeCombo.setFont(MainFrame.FONT_REGULAR);
        reportTypeCombo.addActionListener(e -> updateInputLabel());

        // input label and field
        inputLabel = new JLabel("Student ID:");
        inputLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        inputLabel.setFont(MainFrame.FONT_REGULAR);

        inputField = new JTextField(15);
        inputField.setBackground(MainFrame.COLOR_BG);
        inputField.setForeground(MainFrame.COLOR_TEXT);
        inputField.setCaretColor(MainFrame.COLOR_TEXT);
        inputField.setFont(MainFrame.FONT_REGULAR);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JButton generateBtn = createButton("Generate Report", MainFrame.COLOR_ACCENT);
        generateBtn.addActionListener(e -> generateReport());

        JButton clearBtn = createButton("Clear", MainFrame.COLOR_BORDER);
        clearBtn.addActionListener(e -> reportArea.setText(""));

        toolbar.add(typeLabel);
        toolbar.add(reportTypeCombo);
        toolbar.add(Box.createHorizontalStrut(15));
        toolbar.add(inputLabel);
        toolbar.add(inputField);
        toolbar.add(generateBtn);
        toolbar.add(clearBtn);

        add(toolbar, BorderLayout.NORTH);
    }


    // ===== REPORT AREA =====
    private void buildReportArea() {
        reportArea = new JTextArea();
        reportArea.setBackground(MainFrame.COLOR_BG);
        reportArea.setForeground(MainFrame.COLOR_TEXT);
        reportArea.setFont(MainFrame.FONT_MONO);
        reportArea.setEditable(false);
        reportArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        reportArea.setText(getWelcomeMessage());

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBackground(MainFrame.COLOR_BG);
        scrollPane.getViewport().setBackground(MainFrame.COLOR_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);
    }


    // ===== UPDATE INPUT LABEL =====
    // changes the input label based on selected report type
    private void updateInputLabel() {
        String selected = (String) reportTypeCombo.getSelectedItem();
        if (selected == null) return;

        switch (selected) {
            case "Student Transcript":
                inputLabel.setText("Student ID:");
                inputField.setEnabled(true);
                break;
            case "Course Roster":
                inputLabel.setText("Course Code:");
                inputField.setEnabled(true);
                break;
            default:
                inputLabel.setText("N/A:");
                inputField.setText("");
                inputField.setEnabled(false);
                break;
        }
    }


    // ===== GENERATE REPORT =====
    private void generateReport() {
        String selected = (String) reportTypeCombo.getSelectedItem();
        if (selected == null) return;

        reportArea.setText(""); // clear previous report

        switch (selected) {
            case "Student Transcript":
                generateTranscript();
                break;
            case "Course Roster":
                generateCourseRoster();
                break;
            case "System Summary":
                generateSystemSummary();
                break;
            case "Dean's List":
                generateDeansList();
                break;
            case "All Enrollments":
                generateAllEnrollments();
                break;
        }
    }


    // ===== TRANSCRIPT =====
    private void generateTranscript() {
        String studentId = inputField.getText().trim().toUpperCase();
        if (studentId.isEmpty()) {
            mainFrame.showError("Please enter a Student ID.");
            return;
        }

        Student student = DataStore.findStudentById(studentId);
        if (student == null) {
            mainFrame.showError("Student not found: " + studentId);
            return;
        }

        List<Enrollment> enrollments =
                EnrollmentService.getStudentEnrollments(studentId);

        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("         GIMPA STUDENT TRANSCRIPT               \n");
        sb.append("================================================\n");
        sb.append(String.format("  Student  : %s%n", student.getName()));
        sb.append(String.format("  ID       : %s%n", student.getStudentId()));
        sb.append(String.format("  Program  : %s%n", student.getProgram()));
        sb.append(String.format("  Year     : %d%n", student.getYearOfStudy()));
        sb.append("------------------------------------------------\n");
        sb.append(String.format("  %-12s %-25s %-7s %-6s%n",
                "Code", "Title", "Credits", "Grade"));
        sb.append("------------------------------------------------\n");

        double totalPoints = 0;
        int totalCredits   = 0;

        for (Enrollment e : enrollments) {
            Course course = e.getCourse();
            String grade  = e.isGraded() ?
                    EnrollmentService.getLetterGrade(e.getGrade()) : "Pending";

            sb.append(String.format("  %-12s %-25s %-7d %-6s%n",
                    course.getCourseCode(),
                    truncate(course.getCourseTitle(), 24),
                    course.getCredits(),
                    grade));

            if (e.isGraded()) {
                totalPoints += EnrollmentService.convertToGradePoint(
                        e.getGrade()) * course.getCredits();
                totalCredits += course.getCredits();
            }
        }

        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
        sb.append("------------------------------------------------\n");
        sb.append(String.format("  GPA      : %.2f%n", gpa));
        sb.append(String.format("  Credits  : %d%n", totalCredits));
        sb.append("================================================\n");

        reportArea.setText(sb.toString());
        mainFrame.showStatus("Transcript generated for " + student.getName(), true);
    }


    // ===== COURSE ROSTER =====
    private void generateCourseRoster() {
        String courseCode = inputField.getText().trim().toUpperCase();
        if (courseCode.isEmpty()) {
            mainFrame.showError("Please enter a Course Code.");
            return;
        }

        Course course = DataStore.findCourseByCode(courseCode);
        if (course == null) {
            mainFrame.showError("Course not found: " + courseCode);
            return;
        }

        List<Student> students = EnrollmentService.getCourseStudents(courseCode);

        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("              COURSE ROSTER                     \n");
        sb.append("================================================\n");
        sb.append(String.format("  Course   : %s%n", course.getCourseTitle()));
        sb.append(String.format("  Code     : %s%n", course.getCourseCode()));
        sb.append(String.format("  Credits  : %d%n", course.getCredits()));
        sb.append(String.format("  Instructor: %s%n", course.getInstructor()));
        sb.append("------------------------------------------------\n");
        sb.append(String.format("  %-5s %-12s %-25s %-6s%n",
                "#", "Student ID", "Name", "Grade"));
        sb.append("------------------------------------------------\n");

        int count = 1;
        for (Student s : students) {
            List<Enrollment> enrollments =
                    EnrollmentService.getStudentEnrollments(s.getStudentId());
            String grade = "Pending";
            for (Enrollment e : enrollments) {
                if (e.getCourse().getCourseCode().equals(courseCode) && e.isGraded()) {
                    grade = EnrollmentService.getLetterGrade(e.getGrade());
                }
            }
            sb.append(String.format("  %-5d %-12s %-25s %-6s%n",
                    count++,
                    s.getStudentId(),
                    truncate(s.getName(), 24),
                    grade));
        }

        sb.append("------------------------------------------------\n");
        sb.append(String.format("  Total Enrolled: %d%n", students.size()));
        sb.append("================================================\n");

        reportArea.setText(sb.toString());
        mainFrame.showStatus("Roster generated for " + course.getCourseTitle(), true);
    }


    // ===== SYSTEM SUMMARY =====
    private void generateSystemSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("             SYSTEM SUMMARY                     \n");
        sb.append("================================================\n");
        sb.append(String.format("  Total Students    : %d%n",
                DataStore.getStudentCount()));
        sb.append(String.format("  Total Courses     : %d%n",
                DataStore.getCourseCount()));
        sb.append(String.format("  Total Enrollments : %d%n",
                EnrollmentService.getAllEnrollments().size()));
        sb.append("------------------------------------------------\n");

        // students by year
        sb.append("  Students By Year:%n".formatted());
        for (int year = 1; year <= 4; year++) {
            final int y = year;
            long count = DataStore.getAllStudents().stream()
                    .filter(s -> s.getYearOfStudy() == y)
                    .count();
            sb.append(String.format("    Year %d : %d students%n", year, count));
        }

        sb.append("================================================\n");
        reportArea.setText(sb.toString());
        mainFrame.showStatus("System summary generated", true);
    }


    // ===== DEAN'S LIST =====
    private void generateDeansList() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("              DEAN'S LIST (GPA >= 3.5)          \n");
        sb.append("================================================\n");
        sb.append(String.format("  %-5s %-12s %-25s %-6s%n",
                "#", "Student ID", "Name", "GPA"));
        sb.append("------------------------------------------------\n");

        int rank = 1;
        for (Student s : DataStore.getAllStudents()) {
            double gpa = EnrollmentService.calculateGPA(s.getStudentId());
            if (gpa >= 3.5) {
                sb.append(String.format("  %-5d %-12s %-25s %.2f%n",
                        rank++,
                        s.getStudentId(),
                        truncate(s.getName(), 24),
                        gpa));
            }
        }

        if (rank == 1) {
            sb.append("  No students qualify for Dean's List yet.\n");
        }

        sb.append("================================================\n");
        reportArea.setText(sb.toString());
        mainFrame.showStatus("Dean's List generated", true);
    }


    // ===== ALL ENROLLMENTS =====
    private void generateAllEnrollments() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("            ALL ENROLLMENTS                     \n");
        sb.append("================================================\n");
        sb.append(String.format("  %-12s %-20s %-10s %-6s%n",
                "Student ID", "Student Name", "Course", "Grade"));
        sb.append("------------------------------------------------\n");

        for (Enrollment e : EnrollmentService.getAllEnrollments()) {
            String grade = e.isGraded() ?
                    EnrollmentService.getLetterGrade(e.getGrade()) : "Pending";
            sb.append(String.format("  %-12s %-20s %-10s %-6s%n",
                    e.getStudent().getStudentId(),
                    truncate(e.getStudent().getName(), 19),
                    e.getCourse().getCourseCode(),
                    grade));
        }

        sb.append("------------------------------------------------\n");
        sb.append(String.format("  Total: %d enrollments%n",
                EnrollmentService.getAllEnrollments().size()));
        sb.append("================================================\n");

        reportArea.setText(sb.toString());
        mainFrame.showStatus("All enrollments report generated", true);
    }


    // ===== WELCOME MESSAGE =====
    private String getWelcomeMessage() {
        return
                "================================================\n" +
                        "    GIMPA STUDENT MANAGEMENT SYSTEM            \n" +
                        "    REPORTS & ANALYTICS                        \n" +
                        "================================================\n\n" +
                        "  Select a report type above and click         \n" +
                        "  'Generate Report' to view results.           \n\n" +
                        "  Available Reports:                           \n" +
                        "  - Student Transcript  (enter Student ID)     \n" +
                        "  - Course Roster       (enter Course Code)    \n" +
                        "  - System Summary                             \n" +
                        "  - Dean's List         (GPA >= 3.5)           \n" +
                        "  - All Enrollments                            \n" +
                        "================================================\n";
    }


    // ===== HELPER =====
    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }


    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(MainFrame.FONT_REGULAR);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color hoverColor = color.brighter();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }
}