 package com.gimpa.studentsystem.exception;

    // The base class for all our custom errors
    public class StudentSystemException extends Exception {
        public StudentSystemException(String message) {
            super(message);
        }
    }
