package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.Enrollment;
import com.gimpa.studentsystem.service.DataStore;
import com.gimpa.studentsystem.service.EnrollmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * EnrollmentPanel — manages student enrollments and grades in the GUI.
 * Phase 6 - GUI Development
 */
public class EnrollmentPanel extends JPanel {

    private final MainFrame mainFrame;
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;


    public EnrollmentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(MainFrame.COLOR_BG);
        setLayout(new BorderLayout(0, 0));
        buildToolbar();
        buildTable();
        refreshTable();
    }


    private void buildToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(MainFrame.COLOR_PANEL);
        toolbar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, MainFrame.COLOR_BORDER));

        JButton enrollBtn = createButton("Enroll Student", MainFrame.COLOR_SUCCESS);
        enrollBtn.addActionListener(e -> showEnrollDialog());

        JButton gradeBtn = createButton("Record Grade", MainFrame.COLOR_ACCENT);
        gradeBtn.addActionListener(e -> showGradeDialog());

        JButton refreshBtn = createButton("Refresh", MainFrame.COLOR_BORDER);
        refreshBtn.addActionListener(e -> refreshTable());

        toolbar.add(enrollBtn);
        toolbar.add(gradeBtn);
        toolbar.add(refreshBtn);

        add(toolbar, BorderLayout.NORTH);
    }


    private void buildTable() {
        String[] columns = {
                "Student ID", "Student Name",
                "Course Code", "Course Title",
                "Grade", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setBackground(MainFrame.COLOR_BG);
        enrollmentTable.setForeground(MainFrame.COLOR_TEXT);
        enrollmentTable.setFont(MainFrame.FONT_REGULAR);
        enrollmentTable.setRowHeight(35);
        enrollmentTable.setShowGrid(false);
        enrollmentTable.setIntercellSpacing(new Dimension(0, 0));
        enrollmentTable.setSelectionBackground(MainFrame.COLOR_ACCENT);
        enrollmentTable.setSelectionForeground(Color.WHITE);
        enrollmentTable.setFillsViewportHeight(true);

        JTableHeader header = enrollmentTable.getTableHeader();
        header.setBackground(MainFrame.COLOR_TABLE_HEADER);
        header.setForeground(MainFrame.COLOR_TEXT);
        header.setFont(MainFrame.FONT_HEADING);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        enrollmentTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        enrollmentTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        enrollmentTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        enrollmentTable.getColumnModel().getColumn(3).setPreferredWidth(220);
        enrollmentTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        enrollmentTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setBackground(MainFrame.COLOR_BG);
        scrollPane.getViewport().setBackground(MainFrame.COLOR_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(MainFrame.COLOR_PANEL);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, MainFrame.COLOR_BORDER));

        JLabel countLabel = new JLabel(
                "  Total Enrollments: " +
                        EnrollmentService.getAllEnrollments().size());
        countLabel.setFont(MainFrame.FONT_SMALL);
        countLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        bottomPanel.add(countLabel);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }


    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Enrollment> enrollments = EnrollmentService.getAllEnrollments();
        for (Enrollment e : enrollments) {
            String grade  = e.isGraded() ?
                    String.valueOf(e.getGrade()) : "Pending";
            String status = e.isGraded() ? "Completed" : "Enrolled";
            tableModel.addRow(new Object[]{
                    e.getStudent().getStudentId(),
                    e.getStudent().getName(),
                    e.getCourse().getCourseCode(),
                    e.getCourse().getCourseTitle(),
                    grade,
                    status
            });
        }
    }


    private void showEnrollDialog() {
        if (DataStore.getAllStudents().isEmpty()) {
            mainFrame.showError("No students found. Please add students first.");
            return;
        }
        if (DataStore.getAllCourses().isEmpty()) {
            mainFrame.showError("No courses found. Please add courses first.");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(MainFrame.COLOR_PANEL);

        JLabel studentLabel = new JLabel("Student ID:");
        studentLabel.setForeground(MainFrame.COLOR_TEXT);
        studentLabel.setFont(MainFrame.FONT_REGULAR);

        JLabel courseLabel = new JLabel("Course Code:");
        courseLabel.setForeground(MainFrame.COLOR_TEXT);
        courseLabel.setFont(MainFrame.FONT_REGULAR);

        JTextField studentField = new JTextField();
        studentField.setBackground(MainFrame.COLOR_BG);
        studentField.setForeground(MainFrame.COLOR_TEXT);
        studentField.setFont(MainFrame.FONT_REGULAR);

        JTextField courseField = new JTextField();
        courseField.setBackground(MainFrame.COLOR_BG);
        courseField.setForeground(MainFrame.COLOR_TEXT);
        courseField.setFont(MainFrame.FONT_REGULAR);

        panel.add(studentLabel);
        panel.add(studentField);
        panel.add(courseLabel);
        panel.add(courseField);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel,
                "Enroll Student in Course",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String studentId  = studentField.getText().trim().toUpperCase();
            String courseCode = courseField.getText().trim().toUpperCase();

            if (studentId.isEmpty() || courseCode.isEmpty()) {
                mainFrame.showError("Both Student ID and Course Code are required.");
                return;
            }

            EnrollmentService.enrollStudent(studentId, courseCode);
            refreshTable();
            mainFrame.showStatus("Enrollment processed successfully", true);
        }
    }


    private void showGradeDialog() {
        int row = enrollmentTable.getSelectedRow();
        if (row == -1) {
            mainFrame.showError("Please select an enrollment to grade.");
            return;
        }

        String studentId   = (String) tableModel.getValueAt(row, 0);
        String studentName = (String) tableModel.getValueAt(row, 1);
        String courseCode  = (String) tableModel.getValueAt(row, 2);
        String courseTitle = (String) tableModel.getValueAt(row, 3);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBackground(MainFrame.COLOR_PANEL);

        JLabel studentLbl = new JLabel("Student:");
        studentLbl.setForeground(MainFrame.COLOR_TEXT);
        JLabel studentVal = new JLabel(studentName);
        studentVal.setForeground(MainFrame.COLOR_ACCENT);

        JLabel courseLbl = new JLabel("Course:");
        courseLbl.setForeground(MainFrame.COLOR_TEXT);
        JLabel courseVal = new JLabel(courseTitle);
        courseVal.setForeground(MainFrame.COLOR_ACCENT);

        JLabel gradeLbl = new JLabel("Grade (0-100):");
        gradeLbl.setForeground(MainFrame.COLOR_TEXT);
        JTextField gradeField = new JTextField();
        gradeField.setBackground(MainFrame.COLOR_BG);
        gradeField.setForeground(MainFrame.COLOR_TEXT);
        gradeField.setFont(MainFrame.FONT_REGULAR);

        panel.add(studentLbl); panel.add(studentVal);
        panel.add(courseLbl);  panel.add(courseVal);
        panel.add(gradeLbl);   panel.add(gradeField);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel,
                "Record Grade",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double grade = Double.parseDouble(gradeField.getText().trim());
                if (grade < 0 || grade > 100) {
                    mainFrame.showError("Grade must be between 0 and 100.");
                    return;
                }
                EnrollmentService.recordGrade(studentId, courseCode, grade);
                refreshTable();
                mainFrame.showStatus("Grade recorded successfully", true);
            } catch (NumberFormatException ex) {
                mainFrame.showError("Please enter a valid number for grade.");
            }
        }
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