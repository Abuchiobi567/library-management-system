package com.library.exception;

// LibraryException Class

//This is a CUSTOM EXCEPTION class for general library errors.

// It EXTENDS the Exception class, which means it is a "checked exception".

//Custom exceptions allow us to create meaningful error messages that are specific to our library application. Instead of using generic

// Java exceptions, we can throw LibraryException with a message that explains exactly what went wrong.

// This is the BASE exception class - other specific exceptions will extend this class.

public class LibraryException extends Exception {

    // Constructor that takes an error message.
    public LibraryException(String message) {
        super(message); // Pass the message to the parent Exception class
    }

    // Constructor that takes an error message and a cause.
    // This is useful when one exception causes another.
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
