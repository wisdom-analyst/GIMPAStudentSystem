package com.gimpa.studentsystem.ui;

import com.gimpa.studentsystem.database.DatabaseManager;
import com.gimpa.studentsystem.service.DataStore;
import com.gimpa.studentsystem.service.EnrollmentService;
import com.gimpa.studentsystem.util.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * MainFrame — the main application window of the GIMPA system.
 * Uses Swing to build a modern tabbed GUI interface.
 * Now integrated with SQLite database for persistent storage.
 * Phase 6 - GUI Development + JDBC Database Integration
 */
public class MainFrame extends JFrame {

    // ===== COLOR SCHEME =====
    public static final Color COLOR_BG           = new Color(18, 18, 28);
    public static final Color COLOR_PANEL        = new Color(28, 28, 42);
    public static final Color COLOR_ACCENT       = new Color(99, 102, 241);
    public static final Color COLOR_ACCENT_HOVER = new Color(129, 140, 248);
    public static final Color COLOR_TEXT         = new Color(226, 232, 240);
    public static final Color COLOR_TEXT_DIM     = new Color(148, 163, 184);
    public static final Color COLOR_SUCCESS      = new Color(34, 197, 94);
    public static final Color COLOR_ERROR        = new Color(239, 68, 68);
    public static final Color COLOR_BORDER       = new Color(51, 51, 74);
    public static final Color COLOR_TABLE_HEADER = new Color(45, 45, 65);


    // ===== FONT SCHEME =====
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Consolas", Font.PLAIN, 12);


    // ===== PANELS =====
    private StudentPanel    studentPanel;
    private CoursePanel     coursePanel;
    private EnrollmentPanel enrollmentPanel;
    private ReportPanel     reportPanel;
    private StatusBar       statusBar;


    // ===== CONSTRUCTOR =====
    public MainFrame() {
        setupFrame();
        setupMenuBar();
        setupContent();
        setupStatusBar();
        setupWindowClose();
        setVisible(true);
    }


    private void setupFrame() {
        setTitle("GIMPA Student & Course Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
    }


    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COLOR_PANEL);
        menuBar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, COLOR_BORDER));

        // ── File Menu ──
        JMenu fileMenu = createMenu("File");

        JMenuItem saveItem = createMenuItem("💾  Save Data");
        saveItem.addActionListener(e -> saveData());

        JMenuItem loadItem = createMenuItem("📂  Load Data");
        loadItem.addActionListener(e -> loadData());

        JMenuItem exportItem = createMenuItem("📤  Export to CSV");
        exportItem.addActionListener(e -> exportData());

        JMenuItem exitItem = createMenuItem("🚪  Exit");
        exitItem.addActionListener(e -> exitApplication());

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // ── Help Menu ──
        JMenu helpMenu = createMenu("Help");

        JMenuItem aboutItem = createMenuItem("ℹ️  About");
        aboutItem.addActionListener(e -> showAbout());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }


    private void setupContent() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_PANEL);
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel(
                "GIMPA Student & Course Management System");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_ACCENT);

        JLabel subtitleLabel = new JLabel(
                "Object Oriented Programming 1  |  JDBC Database Integration");
        subtitleLabel.setFont(FONT_SMALL);
        subtitleLabel.setForeground(COLOR_TEXT_DIM);

        JPanel titleStack = new JPanel();
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.setBackground(COLOR_PANEL);
        titleStack.add(titleLabel);
        titleStack.add(Box.createVerticalStrut(4));
        titleStack.add(subtitleLabel);

        headerPanel.add(titleStack, BorderLayout.WEST);

        // summary badges
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        summaryPanel.setBackground(COLOR_PANEL);
        summaryPanel.add(createSummaryBadge("Students",
                String.valueOf(DataStore.getStudentCount())));
        summaryPanel.add(createSummaryBadge("Courses",
                String.valueOf(DataStore.getCourseCount())));

        headerPanel.add(summaryPanel, BorderLayout.EAST);

        // tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(COLOR_BG);
        tabbedPane.setForeground(COLOR_TEXT);
        tabbedPane.setFont(FONT_HEADING);

        studentPanel    = new StudentPanel(this);
        coursePanel     = new CoursePanel(this);
        enrollmentPanel = new EnrollmentPanel(this);
        reportPanel     = new ReportPanel(this);

        tabbedPane.addTab("👤  Students",    studentPanel);
        tabbedPane.addTab("📚  Courses",     coursePanel);
        tabbedPane.addTab("📋  Enrollments", enrollmentPanel);
        tabbedPane.addTab("📊  Reports",     reportPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }


    private void setupStatusBar() {
        statusBar = new StatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }


    private void setupWindowClose() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }


    // ===== ACTIONS =====

    // saves data to both text files and database
    private void saveData() {
        try {
            FileManager.saveData(); // keep text file backup
            showStatus("Data saved successfully", true);
        } catch (Exception e) {
            showError("Error saving data: " + e.getMessage());
        }
    }


    // loads data from database
    private void loadData() {
        try {
            DataStore.initializeFromDatabase();
            refreshAllPanels();
            showStatus("Data loaded from database successfully", true);
        } catch (Exception e) {
            showError("Error loading data: " + e.getMessage());
        }
    }


    private void exportData() {
        String[] options = {"Students", "Courses", "Enrollments"};
        String type = (String) JOptionPane.showInputDialog(this,
                "Select data to export:",
                "Export Data",
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (type != null) {
            String filename = JOptionPane.showInputDialog(this,
                    "Enter filename (e.g. students.csv):");
            if (filename != null && !filename.trim().isEmpty()) {
                try {
                    FileManager.exportToCSV(filename, type.toLowerCase());
                    showStatus("Exported " + type + " to " + filename, true);
                } catch (Exception e) {
                    showError("Export failed: " + e.getMessage());
                }
            }
        }
    }


    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Exit the system?",
                "Exit", JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            // disconnect from database cleanly before exit
            DatabaseManager.disconnect();
            System.exit(0);
        }
        // NO or CANCEL — stay in app
    }


    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "GIMPA Student & Course Management System\n" +
                        "Version 2.0\n\n" +
                        "Object Oriented Programming 1\n" +
                        "Phase 6 — GUI + JDBC Database Integration\n\n" +
                        "Built with Java Swing + SQLite",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }


    // ===== HELPER METHODS =====

    public void refreshAllPanels() {
        studentPanel.refreshTable();
        coursePanel.refreshTable();
        enrollmentPanel.refreshTable();
    }

    public void showStatus(String message, boolean success) {
        statusBar.setMessage(message, success);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Error", JOptionPane.ERROR_MESSAGE);
        statusBar.setMessage(message, false);
    }

    private JMenu createMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(COLOR_TEXT);
        menu.setFont(FONT_REGULAR);
        menu.getPopupMenu().setBackground(COLOR_PANEL);
        return menu;
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(COLOR_PANEL);
        item.setForeground(COLOR_TEXT);
        item.setFont(FONT_REGULAR);
        item.setBorder(new EmptyBorder(8, 15, 8, 15));
        return item;
    }

    private JPanel createSummaryBadge(String label, String count) {
        JPanel badge = new JPanel();
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setBackground(COLOR_BG);
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));

        JLabel countLabel = new JLabel(count, SwingConstants.CENTER);
        countLabel.setFont(FONT_HEADING);
        countLabel.setForeground(COLOR_ACCENT);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(label, SwingConstants.CENTER);
        nameLabel.setFont(FONT_SMALL);
        nameLabel.setForeground(COLOR_TEXT_DIM);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        badge.add(countLabel);
        badge.add(nameLabel);
        return badge;
    }


    // ===== MAIN METHOD =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // cross platform look and feel
            // preserves our custom dark theme colors
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // initialize database and load all data on startup
            DataStore.initializeFromDatabase();

            // show login dialog first
            LoginDialog loginDialog = new LoginDialog();
            loginDialog.setVisible(true);

            if (loginDialog.isLoginSuccessful()) {
                new MainFrame();
            } else {
                // disconnect database cleanly if login cancelled
                DatabaseManager.disconnect();
                System.out.println("Login cancelled. Exiting system.");
                System.exit(0);
            }
        });
    }
}