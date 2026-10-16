package com.library.model;

/**
 * Member Abstract Class
 *
 * This is an ABSTRACT class that represents a library member.
 * It contains common properties that all members share, such as
 * ID, name, email, and phone number.
 *
 * Different types of members (Student, Staff) will extend this class
 * and add their own specific properties.
 * This demonstrates ABSTRACTION - we define common behaviour here,
 * and let subclasses provide specific implementations.
 */
public abstract class Member implements Persistable {

    // INSTANCE VARIABLES
    // These are the properties that every member has

    /** Unique identifier for this member */
    protected String memberId;

    /** The full name of the member */
    protected String name;

    /** The email address of the member */
    protected String email;

    /** The phone number of the member */
    protected String phoneNumber;

    /** The date when the member registered (stored as string for simplicity) */
    protected String registrationDate;

    /** The maximum number of books this member can borrow */
    protected int maxBooksAllowed;

    /** The current number of books the member has borrowed */
    protected int currentBooksCount;

    //CONSTRUCTORS 

    /**
     * Default constructor
     * Creates an empty Member with default values.
     * Needed for loading data from files.
     */
    public Member() {
        this.memberId = "";
        this.name = "";
        this.email = "";
        this.phoneNumber = "";
        this.registrationDate = "";
        this.maxBooksAllowed = 3; // Default limit
        this.currentBooksCount = 0;
    }

    /**
     * Parameterized constructor
     * Creates a Member with the specified values.
     */
    public Member(String memberId, String name, String email,
                  String phoneNumber, String registrationDate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.maxBooksAllowed = 3; // Default limit, subclasses can change this
        this.currentBooksCount = 0;
    }

    //  GETTER METHODS

    /**
     * Gets the member's unique ID
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * Gets the member's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the member's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the member's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the registration date
     *
     */
    public String getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Gets the maximum books this member can borrow
     */
    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    /**
     * Gets the current number of books the member has borrowed
     */
    public int getCurrentBooksCount() {
        return currentBooksCount;
    }

    //  SETTER METHODS

    /**
     * Sets the member's unique ID
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * Sets the member's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the member's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the member's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the registration date
     */
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Sets the maximum books allowed
     */
    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }

    /**
     * Sets the current books count
     */
    public void setCurrentBooksCount(int currentBooksCount) {
        this.currentBooksCount = currentBooksCount;
    }

    // UTILITY METHODS

    /**
     * Checks if the member can borrow more books.
     * Compares current count with the maximum allowed.
     */
    public boolean canBorrowMore() {
        return currentBooksCount < maxBooksAllowed;
    }

    /**
     * Increases the borrowed books count by one.
     * Called when a member borrows a book.
     */
    public void incrementBooksCount() {
        this.currentBooksCount = this.currentBooksCount + 1;
    }

    /**
     * Decreases the borrowed books count by one.
     * Called when a member returns a book.
     */
    public void decrementBooksCount() {
        if (this.currentBooksCount > 0) {
            this.currentBooksCount = this.currentBooksCount - 1;
        }
    }

    //  ABSTRACT METHODS 
    // These methods MUST be implemented by subclasses

    /**
     * Gets the type of this member (e.g., "Student", "Staff")
     * Each subclass will return its own type.
     */
    public abstract String getMemberType();

    /**
     * Gets detailed information about this member.
     * Each subclass will format this based on its specific properties.
     */
    public abstract String getDetails();

    /**
     * Calculates any special discounts or benefits for this member type.
     * Different member types may have different benefits.

     */
    public abstract int getDiscountPercentage();

    // COMMON METHODS 

    /**
     * Returns a simple string representation of this member.
     */
    @Override
    public String toString() {
        return memberId + " - " + name + " (" + getMemberType() + ")";
    }
}
