package com.gimpa.studentsystem.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// StatusBar — bottom bar showing status messages and live clock.
public class StatusBar extends JPanel {

    // label that shows status messages on the left
    private final JLabel messageLabel;

    // label that shows the live clock on the right
    private final JLabel timeLabel;


    // ===== CONSTRUCTOR =====
    public StatusBar() {
        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_PANEL);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, MainFrame.COLOR_BORDER),
                new EmptyBorder(6, 15, 6, 15)
        ));
        setPreferredSize(new Dimension(getWidth(), 35));

        // ── Left — status message ──
        messageLabel = new JLabel("Ready");
        messageLabel.setFont(MainFrame.FONT_SMALL);
        messageLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        add(messageLabel, BorderLayout.WEST);

        // ── Right — live clock ──
        timeLabel = new JLabel();
        timeLabel.setFont(MainFrame.FONT_MONO);
        timeLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        add(timeLabel, BorderLayout.EAST);

        // timer updates the clock every second
        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();
        updateClock(); // show immediately on load
    }


    // updates the clock label with current time
    private void updateClock() {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("EEE dd MMM yyyy  HH:mm:ss"));
        timeLabel.setText(time);
    }


    // shows a message in the status bar
    // green for success, red for error
    public void setMessage(String message, boolean success) {
        messageLabel.setText("  " + message);
        messageLabel.setForeground(
                success ? MainFrame.COLOR_SUCCESS : MainFrame.COLOR_ERROR
        );

        // reset back to default after 4 seconds
        Timer reset = new Timer(4000, e -> {
            messageLabel.setText("  Ready");
            messageLabel.setForeground(MainFrame.COLOR_TEXT_DIM);
        });
        reset.setRepeats(false);
        reset.start();
    }
}