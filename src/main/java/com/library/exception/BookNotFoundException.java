package com.library.exception;

// BookNotFoundException Class

// This custom exception is thrown when a book cannot be found in the library. For example, when trying to borrow or view a book that doesn't exist.
//It EXTENDS LibraryException, which shows INHERITANCE in exception handling.

public class BookNotFoundException extends LibraryException {

    // Constructor that takes the book ID that was not found.
     
    public BookNotFoundException(String bookId) {
        super("Book not found with ID: " + bookId);
    }
}
