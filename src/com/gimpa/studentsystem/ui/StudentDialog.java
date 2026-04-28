package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * StudentDialog — popup form for adding or editing a student.
 * Phase 6 - GUI Development
 */
public class StudentDialog extends JDialog {

    // form fields
    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField programField;
    private JComboBox<Integer> yearCombo;

    // result tracking
    private boolean confirmed = false;
    private Student student;


    // ===== CONSTRUCTOR =====
    // if studentToEdit is null — we are ADDING a new student
    // if studentToEdit is not null — we are EDITING existing student
    public StudentDialog(Window parent, Student studentToEdit) {
        super(parent, studentToEdit == null ? "Add New Student" : "Edit Student",
                ModalityType.APPLICATION_MODAL);

        this.student = studentToEdit;

        setSize(450, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(MainFrame.COLOR_PANEL);

        buildForm(studentToEdit);
        buildButtons();
    }


    // ===== BUILD FORM =====
    private void buildForm(Student studentToEdit) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(MainFrame.COLOR_PANEL);
        form.setBorder(new EmptyBorder(20, 25, 10, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(8, 5, 8, 5);
        gbc.weightx   = 1.0;

        // title label at top
        JLabel titleLabel = new JLabel(
                studentToEdit == null ? "Add New Student" : "Edit Student Details");
        titleLabel.setFont(MainFrame.FONT_HEADING);
        titleLabel.setForeground(MainFrame.COLOR_ACCENT);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // build each form row
        idField      = addFormRow(form, gbc, "Student ID",   1, "e.g. GIMPA/2024/001");
        nameField    = addFormRow(form, gbc, "Full Name",    2, "e.g. Kwame Mensah");
        emailField   = addFormRow(form, gbc, "Email",        3, "e.g. kwame@gimpa.edu.gh");
        phoneField   = addFormRow(form, gbc, "Phone",        4, "e.g. 0244123456");
        programField = addFormRow(form, gbc, "Program",      5, "e.g. Computer Science");

        // year dropdown
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel yearLabel = new JLabel("Year of Study");
        yearLabel.setFont(MainFrame.FONT_REGULAR);
        yearLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        form.add(yearLabel, gbc);

        gbc.gridx = 1;
        yearCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        yearCombo.setBackground(MainFrame.COLOR_BG);
        yearCombo.setForeground(MainFrame.COLOR_TEXT);
        yearCombo.setFont(MainFrame.FONT_REGULAR);
        form.add(yearCombo, gbc);

        // if editing — prefill all fields with existing data
        if (studentToEdit != null) {
            idField.setText(studentToEdit.getStudentId());
            idField.setEditable(false); // ID cannot be changed
            idField.setBackground(MainFrame.COLOR_TABLE_HEADER);
            nameField.setText(studentToEdit.getName());
            emailField.setText(studentToEdit.getEmail());
            phoneField.setText(studentToEdit.getPhone());
            programField.setText(studentToEdit.getProgram());
            yearCombo.setSelectedItem(studentToEdit.getYearOfStudy());
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

        JButton saveBtn = new JButton(student == null ? "Add Student" : "Save Changes");
        saveBtn.setBackground(MainFrame.COLOR_ACCENT);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(MainFrame.FONT_REGULAR);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(new EmptyBorder(8, 20, 8, 20));
        saveBtn.addActionListener(e -> onSave());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Enter key triggers save
        getRootPane().setDefaultButton(saveBtn);
    }


    // ===== ON SAVE =====
    // validates form and creates/updates student object
    private void onSave() {
        try {
            String id      = idField.getText().trim();
            String name    = nameField.getText().trim();
            String email   = emailField.getText().trim();
            String phone   = phoneField.getText().trim();
            String program = programField.getText().trim();
            int year       = (Integer) yearCombo.getSelectedItem();

            // basic validation — no empty fields
            if (id.isEmpty() || name.isEmpty() || email.isEmpty()
                    || phone.isEmpty() || program.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "All fields are required.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // create or update student object
            if (student == null) {
                // adding new student
                student = new Student(id, name, email, phone, program, year);
            } else {
                // editing existing student
                student.setName(name);
                student.setEmail(email);
                student.setPhone(phone);
                student.setProgram(program);
                student.setYearOfStudy(year);
            }

            confirmed = true;
            dispose(); // close dialog

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // ===== HELPER — ADD FORM ROW =====
    // creates a label + text field pair and adds to form
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

    // returns true if user clicked Save
    public boolean isConfirmed() { return confirmed; }

    // returns the student object created or edited
    public Student getStudent()  { return student; }
}