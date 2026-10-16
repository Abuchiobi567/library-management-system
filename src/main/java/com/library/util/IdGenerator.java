package com.library.util;


 // IdGenerator Utility Class

public class IdGenerator {

    // ============ COUNTERS ============
    // These keep track of the next ID number for each entity type

    /** Counter for book IDs */
    private static int bookCounter = 0;

    /** Counter for ebook IDs */
    private static int ebookCounter = 0;

    /** Counter for member IDs */
    private static int memberCounter = 0;

    /** Counter for loan IDs */
    private static int loanCounter = 0;

    // ============ PRIVATE CONSTRUCTOR ============

    /**
     * Private constructor to prevent instantiation.
     */
    private IdGenerator() {
        // This class should not be instantiated
    }

    // ============ ID GENERATION METHODS ============

    /**
     * Generates a new unique ID for a book.
     * Format: BK001, BK002, etc.
     */
    public static String generateBookId() {
        bookCounter = bookCounter + 1;
        // Format the number with leading zeros (e.g., 001, 002, 003)
        String formattedNumber = String.format("%03d", bookCounter);
        return "BK" + formattedNumber;
    }

    /**
     * Generates a new unique ID for an ebook.
     * Format: EB001, EB002, etc.
     */
    public static String generateEBookId() {
        ebookCounter = ebookCounter + 1;
        String formattedNumber = String.format("%03d", ebookCounter);
        return "EB" + formattedNumber;
    }

    /**
     * Generates a new unique ID for a member.
     * Format: MEM001, MEM002, etc.
     */
    public static String generateMemberId() {
        memberCounter = memberCounter + 1;
        String formattedNumber = String.format("%03d", memberCounter);
        return "MEM" + formattedNumber;
    }

    /**
     * Generates a new unique ID for a loan.
     * Format: LN001, LN002, etc.
     */
    public static String generateLoanId() {
        loanCounter = loanCounter + 1;
        String formattedNumber = String.format("%03d", loanCounter);
        return "LN" + formattedNumber;
    }

    // ============ COUNTER MANAGEMENT METHODS ============
    // These methods are used when loading data from files to ensure
    // new IDs don't conflict with existing ones


    public static void setBookCounter(int value) {
        bookCounter = value;
    }


    public static void setEBookCounter(int value) {
        ebookCounter = value;
    }


    public static void setMemberCounter(int value) {
        memberCounter = value;
    }


    public static void setLoanCounter(int value) {
        loanCounter = value;
    }

    /**
     * Gets the current book counter value.
     *
     * @return The current book counter
     */
    public static int getBookCounter() {
        return bookCounter;
    }

    /**
     * Gets the current ebook counter value.
     *
     * @return The current ebook counter
     */
    public static int getEBookCounter() {
        return ebookCounter;
    }

    /**
     * Gets the current member counter value.
     *
     * @return The current member counter
     */
    public static int getMemberCounter() {
        return memberCounter;
    }

    /**
     * Gets the current loan counter value.
     * @return The current loan counter
     */
    public static int getLoanCounter() {
        return loanCounter;
    }

    /**
     * Resets all counters to zero.
     * Useful for testing purposes.
     */
    public static void resetAllCounters() {
        bookCounter = 0;
        ebookCounter = 0;
        memberCounter = 0;
        loanCounter = 0;
    }
}
