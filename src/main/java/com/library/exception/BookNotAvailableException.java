
package com.library.exception;

// BookNotAvailableException Class

// This custom exception is thrown when someone tries to borrow a book that is already borrowed by another member.
// It EXTENDS LibraryException, which shows INHERITANCE in exception handling.
 
public class BookNotAvailableException extends LibraryException {

    // Constructor that takes the book ID that is not available.
    
    public BookNotAvailableException(String bookId) {
        super("Book is not available for borrowing. Book ID: " + bookId);
    }
}
