package com.gimpa.studentsystem.exception;

public class EntityNotFoundException extends StudentSystemException {
    public EntityNotFoundException(String entityType, String id) {
        super(entityType + " with ID: " + id + " was not found in the system.");
    }
}
