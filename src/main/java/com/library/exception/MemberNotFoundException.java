package com.library.exception;

// MemberNotFoundException Class

// This custom exception is thrown when a member cannot be found in the library. For example, when trying to issue a book to a member that doesn't exist.

// It EXTENDS LibraryException, which shows INHERITANCE in exception handling.

public class MemberNotFoundException extends LibraryException {

    // Constructor that takes the member ID that was not found.

    public MemberNotFoundException(String memberId) {
        super("Member not found with ID: " + memberId);
    }
}
