package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.model.Course;
import com.gimpa.studentsystem.service.DataStore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * CoursePanel — manages all course CRUD operations in the GUI.
 * Phase 6 - GUI Development
 */
public class CoursePanel extends JPanel {

    private final MainFrame mainFrame;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;


    public CoursePanel(MainFrame mainFrame) {
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
        searchBtn.addActionListener(e -> searchCourses());

        JButton clearBtn = createButton("Clear", MainFrame.COLOR_BORDER);
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        JButton addBtn = createButton("Add Course", MainFrame.COLOR_SUCCESS);
        addBtn.addActionListener(e -> showAddDialog());

        JButton editBtn = createButton("Edit", MainFrame.COLOR_ACCENT);
        editBtn.addActionListener(e -> showEditDialog());

        JButton deleteBtn = createButton("Delete", MainFrame.COLOR_ERROR);
        deleteBtn.addActionListener(e -> deleteCourse());

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
        String[] columns = {"Course Code", "Title", "Credits", "Instructor"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setBackground(MainFrame.COLOR_BG);
        courseTable.setForeground(MainFrame.COLOR_TEXT);
        courseTable.setFont(MainFrame.FONT_REGULAR);
        courseTable.setRowHeight(35);
        courseTable.setShowGrid(false);
        courseTable.setIntercellSpacing(new Dimension(0, 0));
        courseTable.setSelectionBackground(MainFrame.COLOR_ACCENT);
        courseTable.setSelectionForeground(Color.WHITE);
        courseTable.setFillsViewportHeight(true);

        JTableHeader header = courseTable.getTableHeader();
        header.setBackground(MainFrame.COLOR_TABLE_HEADER);
        header.setForeground(MainFrame.COLOR_TEXT);
        header.setFont(MainFrame.FONT_HEADING);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        courseTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(280);
        courseTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        courseTable.getColumnModel().getColumn(3).setPreferredWidth(200);

        courseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) showEditDialog();
            }
        });

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBackground(MainFrame.COLOR_BG);
        scrollPane.getViewport().setBackground(MainFrame.COLOR_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(MainFrame.COLOR_PANEL);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, MainFrame.COLOR_BORDER));

        JLabel countLabel = new JLabel(
                "  Total: " + DataStore.getCourseCount() + " courses");
        countLabel.setFont(MainFrame.FONT_SMALL);
        countLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        bottomPanel.add(countLabel);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }


    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Course> courses = DataStore.getAllCourses();
        for (Course c : courses) {
            tableModel.addRow(new Object[]{
                    c.getCourseCode(),
                    c.getCourseTitle(),
                    c.getCredits(),
                    c.getInstructor()
            });
        }
    }


    private void searchCourses() {
        String term = searchField.getText().trim().toLowerCase();
        if (term.isEmpty()) {
            refreshTable();
            return;
        }
        tableModel.setRowCount(0);
        for (Course c : DataStore.getAllCourses()) {
            if (c.getCourseTitle().toLowerCase().contains(term)
                    || c.getCourseCode().toLowerCase().contains(term)
                    || c.getInstructor().toLowerCase().contains(term)) {
                tableModel.addRow(new Object[]{
                        c.getCourseCode(),
                        c.getCourseTitle(),
                        c.getCredits(),
                        c.getInstructor()
                });
            }
        }
    }


    private void showAddDialog() {
        CourseDialog dialog = new CourseDialog(mainFrame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Course course = dialog.getCourse();
            if (DataStore.addCourse(course)) {
                refreshTable();
                mainFrame.showStatus("Course added: " + course.getCourseTitle(), true);
            } else {
                mainFrame.showError("Course code already exists!");
            }
        }
    }


    private void showEditDialog() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            mainFrame.showError("Please select a course to edit.");
            return;
        }
        String courseCode = (String) tableModel.getValueAt(row, 0);
        Course course     = DataStore.findCourseByCode(courseCode);
        if (course != null) {
            CourseDialog dialog = new CourseDialog(mainFrame, course);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                DataStore.updateCourse(dialog.getCourse());
                refreshTable();
                mainFrame.showStatus("Course updated successfully", true);
            }
        }
    }


    private void deleteCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            mainFrame.showError("Please select a course to delete.");
            return;
        }
        String courseCode = (String) tableModel.getValueAt(row, 0);
        String title      = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(mainFrame,
                "Are you sure you want to delete " + title + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DataStore.deleteCourse(courseCode);
            refreshTable();
            mainFrame.showStatus("Course deleted: " + title, true);
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