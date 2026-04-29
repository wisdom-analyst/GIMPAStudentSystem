package com.gimpa.studentsystem.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginDialog — first screen users see when launching the system.
 * Blocks access to main window until valid credentials are entered.
 * Phase 6 - GUI Development + User Authentication
 */
public class LoginDialog extends JDialog {

    // tracks whether login was successful
    private boolean loginSuccessful = false;

    // input fields
    private JTextField usernameField;
    private JPasswordField passwordField;

    // buttons
    private JButton loginButton;
    private JButton cancelButton;

    // failed attempt tracking
    private int failedAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    // hardcoded credentials — will move to database in JDBC phase
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "gimpa2024";


    // ===== CONSTRUCTOR =====
    public LoginDialog() {
        super((Frame) null, "GIMPA Student System — Login", true);
        setSize(440, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(MainFrame.COLOR_BG);

        initComponents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginSuccessful = false;
                dispose();
            }
        });
    }


    // ===== BUILD UI =====
    private void initComponents() {
        setLayout(new BorderLayout());

        // ── HEADER ──
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MainFrame.COLOR_ACCENT);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("GIMPA Student System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel(
                "Please login to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 210, 255));

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // ── FORM ──
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(MainFrame.COLOR_PANEL);
        formPanel.setBorder(new EmptyBorder(25, 30, 15, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(10, 5, 10, 5);
        gbc.weightx = 1.0;

        // username label
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(MainFrame.FONT_REGULAR);
        usernameLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        formPanel.add(usernameLabel, gbc);

        // username field
        gbc.gridx = 1; gbc.weightx = 0.7;
        usernameField = new JTextField(15);
        usernameField.setBackground(MainFrame.COLOR_BG);
        usernameField.setForeground(MainFrame.COLOR_TEXT);
        usernameField.setCaretColor(MainFrame.COLOR_TEXT);
        usernameField.setFont(MainFrame.FONT_REGULAR);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER),
                new EmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(usernameField, gbc);

        // password label
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(MainFrame.FONT_REGULAR);
        passwordLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        formPanel.add(passwordLabel, gbc);

        // password field
        gbc.gridx = 1; gbc.weightx = 0.7;
        passwordField = new JPasswordField(15);
        passwordField.setBackground(MainFrame.COLOR_BG);
        passwordField.setForeground(MainFrame.COLOR_TEXT);
        passwordField.setCaretColor(MainFrame.COLOR_TEXT);
        passwordField.setFont(MainFrame.FONT_REGULAR);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER),
                new EmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(passwordField, gbc);

        // hint label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JLabel hintLabel = new JLabel("Default: admin / gimpa2024");
        hintLabel.setFont(MainFrame.FONT_SMALL);
        hintLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(hintLabel, gbc);

        // ── BUTTONS ──
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(MainFrame.COLOR_PANEL);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, MainFrame.COLOR_BORDER));

        // cancel button — dark grey solid color
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelButton.setBackground(new Color(80, 80, 100));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setOpaque(true);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // login button — solid purple accent color
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(MainFrame.COLOR_ACCENT);
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(cancelButton);
        buttonPanel.add(loginButton);

        // ── ASSEMBLE ──
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // connect button actions
        attachActions();

        // Enter key triggers login
        getRootPane().setDefaultButton(loginButton);

        // auto focus username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }


    // ===== ACTIONS =====
    private void attachActions() {
        loginButton.addActionListener(e -> attemptLogin());

        cancelButton.addActionListener(e -> {
            loginSuccessful = false;
            dispose();
        });

        // Escape key cancels
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(
                e -> {
                    loginSuccessful = false;
                    dispose();
                },
                escapeKey,
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }


    // ===== LOGIN LOGIC =====
    private void attemptLogin() {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = new String(passwordField.getPassword()).trim();

        // check empty fields
        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        if (enteredUsername.equals(ADMIN_USERNAME)
                && enteredPassword.equals(ADMIN_PASSWORD)) {

            // login successful
            loginSuccessful = true;
            dispose();

        } else {

            // login failed
            failedAttempts++;
            int remaining = MAX_ATTEMPTS - failedAttempts;

            if (failedAttempts >= MAX_ATTEMPTS) {
                showError("Too many failed attempts. System locked.");
                loginButton.setEnabled(false);
                passwordField.setEnabled(false);
                usernameField.setEnabled(false);
            } else {
                showError("Invalid username or password.\n"
                        + remaining + " attempt(s) remaining.");
                passwordField.setText("");
                passwordField.requestFocusInWindow();
            }
        }
    }


    // shows error popup
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
    }


    // ===== RESULT =====
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}