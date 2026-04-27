package com.gimpa.studentsystem.util;

import com.gimpa.studentsystem.model.Student;
import com.gimpa.studentsystem.service.DataStore;
import java.io.*;

public class FileManager {
    private static final String STUDENT_FILE = "students_data.txt";

    // SAVES data from the DataStore to a file
    public static void saveAllData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (Student s : DataStore.getAllStudents()) {
                // Save format: ID|Name|Email|Program|Year
                writer.println(s.getStudentId() + "|" + s.getName() + "|" +
                        s.getEmail() + "|" + s.getProgram() + "|" + s.getYearOfStudy());
            }
            System.out.println("[FILE SYSTEM] All data saved successfully.");
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save to file: " + e.getMessage());
        }
    }

    // LOADS data from the file back into the DataStore
    public static void loadAllData() {
        File file = new File(STUDENT_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    Student s = new Student(parts[0], parts[1], parts[2], "N/A", parts[3], Integer.parseInt(parts[4]));
                    DataStore.addStudent(s);
                }
            }
            System.out.println("[FILE SYSTEM] Data loaded from storage.");
        } catch (Exception e) {
            System.out.println("[ERROR] Loading failed: " + e.getMessage());
        }
    }
}