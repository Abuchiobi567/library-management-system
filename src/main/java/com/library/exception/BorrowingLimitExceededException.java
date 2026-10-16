package com.library.exception;

/// BorrowingLimitExceededException Class

// This custom exception is thrown when a member tries to borrow more books than they are allowed. Different member types have different limits.

// It EXTENDS LibraryException, which shows INHERITANCE in exception handling.

public class BorrowingLimitExceededException extends LibraryException {

    // Constructor that takes the member ID and their borrowing limit.
     
    public BorrowingLimitExceededException(String memberId, int limit) {
        super("Member " + memberId + " has reached the maximum borrowing limit of " + limit + " books.");
    }
}
