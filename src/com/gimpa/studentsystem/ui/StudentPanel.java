package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.service.DataStore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * StudentPanel — manages all student CRUD operations in the GUI.
 * Phase 6 - GUI Development
 */
public class StudentPanel extends JPanel {

    private final MainFrame mainFrame;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;


    public StudentPanel(MainFrame mainFrame) {
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

        // search label
        JLabel searchLabel = new JLabel("  Search: ");
        searchLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        searchLabel.setFont(MainFrame.FONT_REGULAR);

        searchField = new JTextField(20);
        searchField.setBackground(MainFrame.COLOR_BG);
        searchField.setForeground(MainFrame.COLOR_TEXT);
        searchField.setCaretColor(MainFrame.COLOR_TEXT);
        searchField.setFont(MainFrame.FONT_REGULAR);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JButton searchBtn = createButton("Search", MainFrame.COLOR_ACCENT);
        searchBtn.addActionListener(e -> searchStudents());

        JButton clearBtn = createButton("Clear", MainFrame.COLOR_BORDER);
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        JButton addBtn = createButton("Add Student", MainFrame.COLOR_SUCCESS);
        addBtn.addActionListener(e -> showAddDialog());

        JButton editBtn = createButton("Edit", MainFrame.COLOR_ACCENT);
        editBtn.addActionListener(e -> showEditDialog());

        JButton deleteBtn = createButton("Delete", MainFrame.COLOR_ERROR);
        deleteBtn.addActionListener(e -> deleteStudent());

        JButton refreshBtn = createButton("Refresh", MainFrame.COLOR_BORDER);
        refreshBtn.addActionListener(e -> refreshTable());

        toolbar.add(searchLabel);
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(clearBtn);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(refreshBtn);

        add(toolbar, BorderLayout.NORTH);
    }


    private void buildTable() {
        String[] columns = {"Student ID", "Name", "Email", "Phone", "Program", "Year"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setBackground(MainFrame.COLOR_BG);
        studentTable.setForeground(MainFrame.COLOR_TEXT);
        studentTable.setFont(MainFrame.FONT_REGULAR);
        studentTable.setRowHeight(35);
        studentTable.setShowGrid(false);
        studentTable.setIntercellSpacing(new Dimension(0, 0));
        studentTable.setSelectionBackground(MainFrame.COLOR_ACCENT);
        studentTable.setSelectionForeground(Color.WHITE);
        studentTable.setFillsViewportHeight(true);

        JTableHeader header = studentTable.getTableHeader();
        header.setBackground(MainFrame.COLOR_TABLE_HEADER);
        header.setForeground(MainFrame.COLOR_TEXT);
        header.setFont(MainFrame.FONT_HEADING);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        studentTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(180);
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(60);

        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) showEditDialog();
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBackground(MainFrame.COLOR_BG);
        scrollPane.getViewport().setBackground(MainFrame.COLOR_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(MainFrame.COLOR_PANEL);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, MainFrame.COLOR_BORDER));

        JLabel countLabel = new JLabel(
                "  Total: " + DataStore.getStudentCount() + " students");
        countLabel.setFont(MainFrame.FONT_SMALL);
        countLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        bottomPanel.add(countLabel);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }


    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = DataStore.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getName(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getProgram(),
                    s.getYearOfStudy()
            });
        }
    }


    private void searchStudents() {
        String term = searchField.getText().trim();
        if (term.isEmpty()) {
            refreshTable();
            return;
        }
        tableModel.setRowCount(0);
        for (Student s : DataStore.searchStudentsByName(term)) {
            tableModel.addRow(new Object[]{
                    s.getStudentId(),
                    s.getName(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getProgram(),
                    s.getYearOfStudy()
            });
        }
    }


    private void showAddDialog() {
        StudentDialog dialog = new StudentDialog(mainFrame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Student student = dialog.getStudent();
            if (DataStore.addStudent(student)) {
                refreshTable();
                mainFrame.showStatus("Student added: " + student.getName(), true);
            } else {
                mainFrame.showError("Student ID already exists!");
            }
        }
    }


    private void showEditDialog() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            mainFrame.showError("Please select a student to edit.");
            return;
        }
        String studentId = (String) tableModel.getValueAt(row, 0);
        Student student  = DataStore.findStudentById(studentId);
        if (student != null) {
            StudentDialog dialog = new StudentDialog(mainFrame, student);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                DataStore.updateStudent(dialog.getStudent());
                refreshTable();
                mainFrame.showStatus("Student updated successfully", true);
            }
        }
    }


    private void deleteStudent() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            mainFrame.showError("Please select a student to delete.");
            return;
        }
        String studentId = (String) tableModel.getValueAt(row, 0);
        String name      = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(mainFrame,
                "Are you sure you want to delete " + name + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DataStore.deleteStudent(studentId);
            refreshTable();
            mainFrame.showStatus("Student deleted: " + name, true);
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
