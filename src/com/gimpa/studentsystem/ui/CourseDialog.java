package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.Course;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * CourseDialog — popup form for adding or editing a course.
 * Phase 6 - GUI Development
 */
public class CourseDialog extends JDialog {

    private JTextField codeField;
    private JTextField titleField;
    private JTextField creditsField;
    private JTextField instructorField;

    private boolean confirmed = false;
    private Course course;


    // ===== CONSTRUCTOR =====
    public CourseDialog(Window parent, Course courseToEdit) {
        super(parent, courseToEdit == null ? "Add New Course" : "Edit Course",
                ModalityType.APPLICATION_MODAL);

        this.course = courseToEdit;

        setSize(450, 360);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(MainFrame.COLOR_PANEL);

        buildForm(courseToEdit);
        buildButtons();
    }


    // ===== BUILD FORM =====
    private void buildForm(Course courseToEdit) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(MainFrame.COLOR_PANEL);
        form.setBorder(new EmptyBorder(20, 25, 10, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        // title at top
        JLabel titleLabel = new JLabel(
                courseToEdit == null ? "Add New Course" : "Edit Course Details");
        titleLabel.setFont(MainFrame.FONT_HEADING);
        titleLabel.setForeground(MainFrame.COLOR_ACCENT);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // form rows
        codeField       = addFormRow(form, gbc, "Course Code",  1, "e.g. SOT104B");
        titleField      = addFormRow(form, gbc, "Course Title", 2, "e.g. OOP Programming");
        creditsField    = addFormRow(form, gbc, "Credits",      3, "e.g. 3");
        instructorField = addFormRow(form, gbc, "Instructor",   4, "e.g. Dr. Mensah");

        // prefill if editing
        if (courseToEdit != null) {
            codeField.setText(courseToEdit.getCourseCode());
            codeField.setEditable(false); // code cannot be changed
            codeField.setBackground(MainFrame.COLOR_TABLE_HEADER);
            titleField.setText(courseToEdit.getCourseTitle());
            creditsField.setText(String.valueOf(courseToEdit.getCredits()));
            instructorField.setText(courseToEdit.getInstructor());
        }

        add(form, BorderLayout.CENTER);
    }


    // ===== BUILD BUTTONS =====
    private void buildButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(MainFrame.COLOR_PANEL);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, MainFrame.COLOR_BORDER));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(MainFrame.COLOR_BORDER);
        cancelBtn.setForeground(MainFrame.COLOR_TEXT);
        cancelBtn.setFont(MainFrame.FONT_REGULAR);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton(course == null ? "Add Course" : "Save Changes");
        saveBtn.setBackground(MainFrame.COLOR_ACCENT);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(MainFrame.FONT_REGULAR);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        saveBtn.addActionListener(e -> onSave());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(saveBtn);
    }


    // ===== ON SAVE =====
    private void onSave() {
        try {
            String code       = codeField.getText().trim();
            String title      = titleField.getText().trim();
            String creditsTxt = creditsField.getText().trim();
            String instructor = instructorField.getText().trim();

            // check no empty fields
            if (code.isEmpty() || title.isEmpty()
                    || creditsTxt.isEmpty() || instructor.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "All fields are required.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // parse credits as number
            int credits;
            try {
                credits = Integer.parseInt(creditsTxt);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Credits must be a number.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // create or update course object
            if (course == null) {
                course = new Course(code, title, credits, instructor);
            } else {
                course.setCourseTitle(title);
                course.setCredits(credits);
                course.setInstructor(instructor);
            }

            confirmed = true;
            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // ===== HELPER — FORM ROW =====
    private JTextField addFormRow(JPanel form, GridBagConstraints gbc,
                                  String label, int row, String placeholder) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(MainFrame.FONT_REGULAR);
        lbl.setForeground(MainFrame.COLOR_TEXT_DIM);
        form.add(lbl, gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField();
        field.setBackground(MainFrame.COLOR_BG);
        field.setForeground(MainFrame.COLOR_TEXT);
        field.setCaretColor(MainFrame.COLOR_TEXT);
        field.setFont(MainFrame.FONT_REGULAR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER),
                new EmptyBorder(6, 10, 6, 10)
        ));
        form.add(field, gbc);
        return field;
    }


    // ===== GETTERS =====
    public boolean isConfirmed() { return confirmed; }
    public Course getCourse()    { return course; }
}