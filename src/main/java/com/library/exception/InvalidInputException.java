package com.library.exception;

// InvalidInputException Class

// This custom exception is thrown when user input is invalid. For example, empty fields, invalid email format, or invalid ISBN.

// It EXTENDS LibraryException, which shows INHERITANCE in exception handling.

public class InvalidInputException extends LibraryException {

    // Constructor that takes a description of what input was invalid.
    public InvalidInputException(String message) {
        super("Invalid input: " + message);
    }
}
