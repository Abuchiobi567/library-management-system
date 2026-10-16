package com.library.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Loan Class
 * This class represents a loan record - when a member borrows a book from the library.
 * It tracks who borrowed what, when they borrowed it, when it's due, and when (if) it was returned.
 * The Loan class implements the Persistable interface so it can be saved to a file.
 */
public class Loan implements Persistable {

    //  INSTANCE VARIABLES 

    /** Unique identifier for this loan record */
    private String loanId;

    /** The ID of the book that was borrowed */
    private String bookId;

    /** The ID of the member who borrowed the book */
    private String memberId;

    /** The date when the book was borrowed */
    private LocalDate borrowDate;

    /** The date when the book is due to be returned */
    private LocalDate dueDate;

    /** The date when the book was actually returned (null if not returned yet) */
    private LocalDate returnDate;

    /** Whether the loan is still active (book not returned) */
    private boolean isActive;

    //  CONSTANTS 

    /** Standard loan period in days */
    private static final int LOAN_PERIOD_DAYS = 14;

    /** Date format used for displaying and storing dates */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //  CONSTRUCTORS 

    /**
     * Default constructor
     * Creates an empty Loan with default values.
     * Needed for loading data from files.
     */
    public Loan() {
        this.loanId = "";
        this.bookId = "";
        this.memberId = "";
        this.borrowDate = null;
        this.dueDate = null;
        this.returnDate = null;
        this.isActive = false;
    }

    /**
     * Parameterized constructor
     * Creates a new Loan with the specified book and member.
     * The borrow date is set to today, and due date is calculated automatically.*/
    public Loan(String loanId, String bookId, String memberId) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = LocalDate.now(); // Set to today's date
        this.dueDate = borrowDate.plusDays(LOAN_PERIOD_DAYS); // Due in 14 days
        this.returnDate = null; // Not returned yet
        this.isActive = true; // Loan is active
    }

    /**
     * Full parameterized constructor
     * Creates a Loan with all values specified (used when loading from file).
     */
    public Loan(String loanId, String bookId, String memberId,
                LocalDate borrowDate, LocalDate dueDate,
                LocalDate returnDate, boolean isActive) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.isActive = isActive;
    }

    // GETTER METHODS 

    /**
     * Gets the loan ID
     * */
    public String getLoanId() {
        return loanId;
    }

    /**
     * Gets the book ID
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Gets the member ID
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * Gets the borrow date
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * Gets the due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Gets the return date
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Checks if the loan is active
     */
    public boolean isActive() {
        return isActive;
    }

    //  SETTER METHODS

    /**
     * Sets the loan ID
     */
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    /**
     * Sets the book ID
     */
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    /**
     * Sets the member ID
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * Sets the borrow date
     */
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    /**
     * Sets the due date
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Sets the return date
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Sets the active status
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    // UTILITY METHODS

    /**
     * Marks the book as returned.
     * Sets the return date to today and marks the loan as inactive.
     */
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.isActive = false;
    }

    /**
     * Checks if the loan is overdue.
     * A loan is overdue if it's active and the current date is past the due date.
     */
    public boolean isOverdue() {
        // If the loan is not active (book returned), it can't be overdue
        if (!isActive) {
            return false;
        }
        // Check if today's date is after the due date
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     * Calculates the number of days the loan is overdue.
     * Returns 0 if the loan is not overdue.
     */
    public long getDaysOverdue() {
        // If not overdue, return 0
        if (!isOverdue()) {
            return 0;
        }
        // Calculate the difference between today and the due date
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * Calculates the fine for an overdue loan.
     * The fine is £0.50 per day overdue.
     */
    public double calculateFine() {
        // Fine rate: 50 pence per day
        double finePerDay = 0.50;
        long daysOverdue = getDaysOverdue();
        return daysOverdue * finePerDay;
    }

    /**
     * Gets a formatted string of the borrow date.
     */
    public String getBorrowDateFormatted() {
        if (borrowDate == null) {
            return "N/A";
        }
        return borrowDate.format(DATE_FORMAT);
    }

    /**
     * Gets a formatted string of the due date.
     */
    public String getDueDateFormatted() {
        if (dueDate == null) {
            return "N/A";
        }
        return dueDate.format(DATE_FORMAT);
    }

    /**
     * Gets a formatted string of the return date.
     */
    public String getReturnDateFormatted() {
        if (returnDate == null) {
            return "Not Returned";
        }
        return returnDate.format(DATE_FORMAT);
    }

    // PERSISTABLE INTERFACE METHODS 

    /**
     * Converts the loan data to a string format for saving to a file.
     * Format: LOAN,loanId,bookId,memberId,borrowDate,dueDate,returnDate,isActive
     */
    @Override
    public String toDataString() {
        String data = "LOAN" + ",";
        data = data + loanId + ",";
        data = data + bookId + ",";
        data = data + memberId + ",";
        data = data + getBorrowDateFormatted() + ",";
        data = data + getDueDateFormatted() + ",";
        data = data + getReturnDateFormatted() + ",";
        data = data + isActive;
        return data;
    }

    /**
     * Loads loan data from a comma-separated string.
     */
    @Override
    public void fromDataString(String data) {
        String[] parts = data.split(",");

        if (parts.length >= 8) {
            this.loanId = parts[1];
            this.bookId = parts[2];
            this.memberId = parts[3];

            // Parse borrow date
            if (!parts[4].equals("N/A")) {
                this.borrowDate = LocalDate.parse(parts[4], DATE_FORMAT);
            }

            // Parse due date
            if (!parts[5].equals("N/A")) {
                this.dueDate = LocalDate.parse(parts[5], DATE_FORMAT);
            }

            // Parse return date
            if (!parts[6].equals("Not Returned")) {
                this.returnDate = LocalDate.parse(parts[6], DATE_FORMAT);
            }

            this.isActive = Boolean.parseBoolean(parts[7]);
        }
    }

    /**
     * Returns a simple string representation of this loan.
     */
    @Override
    public String toString() {
        String status = isActive ? "Active" : "Returned";
        if (isOverdue()) {
            status = "OVERDUE";
        }
        return "Loan " + loanId + ": Book " + bookId + " to Member " + memberId +
               " (Due: " + getDueDateFormatted() + ") - " + status;
    }
}
