package com.library.util;

import com.library.exception.InvalidInputException;

/**
 * InputValidator Utility Class
 * This class contains static methods for validating user input.
 */
public class InputValidator {

    // ============ PRIVATE CONSTRUCTOR ============
    // We make the constructor private because this class should not be instantiated
    // It only contains static utility methods

    // Private constructor to prevent instantiation.
    private InputValidator() {
        // This class should not be instantiated
    }

    // ============ VALIDATION METHODS ============

    //Validates that a string is not null and not empty.
    public static void validateNotEmpty(String value, String fieldName) throws InvalidInputException {
        // Check if value is null
        if (value == null) {
            throw new InvalidInputException(fieldName + " cannot be empty.");
        }
        // Check if value is empty or only whitespace
        if (value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty.");
        }
    }

    //Validates that an email address has a valid format.

    public static void validateEmail(String email) throws InvalidInputException {
        // First check if email is not empty
        validateNotEmpty(email, "Email");

        // Check for @ symbol
        if (!email.contains("@")) {
            throw new InvalidInputException("Email must contain @ symbol.");
        }

        // Check for a dot after the @ symbol
        int atIndex = email.indexOf("@");
        String afterAt = email.substring(atIndex);
        if (!afterAt.contains(".")) {
            throw new InvalidInputException("Email must have a domain (e.g., .com).");
        }

        // Check that @ is not at the start or end
        if (email.startsWith("@") || email.endsWith("@")) {
            throw new InvalidInputException("Email format is invalid.");
        }
    }

    //Validates that a phone number contains only digits and is the right length.

    public static void validatePhoneNumber(String phone) throws InvalidInputException {
        // First check if phone is not empty
        validateNotEmpty(phone, "Phone number");

        // Remove any spaces or dashes for validation
        String cleanPhone = phone.replaceAll("[\\s-]", "");

        // Check if all characters are digits
        for (int i = 0; i < cleanPhone.length(); i++) {
            char c = cleanPhone.charAt(i);
            if (!Character.isDigit(c)) {
                throw new InvalidInputException("Phone number must contain only digits.");
            }
        }

        // Check length (UK numbers are typically 10-11 digits)
        if (cleanPhone.length() < 10 || cleanPhone.length() > 15) {
            throw new InvalidInputException("Phone number must be between 10 and 15 digits.");
        }
    }

    // Validates that a year is within a reasonable range.

    public static void validateYear(int year) throws InvalidInputException {
        // Books should be published between 1450 (printing press) and current year
        int currentYear = java.time.Year.now().getValue();

        if (year < 1450) {
            throw new InvalidInputException("Publication year cannot be before 1450.");
        }
        if (year > currentYear) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
    }

    //Validates that a positive integer is provided.

    public static void validatePositiveNumber(int value, String fieldName) throws InvalidInputException {
        if (value <= 0) {
            throw new InvalidInputException(fieldName + " must be a positive number.");
        }
    }

    //Validates that a positive double is provided.

    public static void validatePositiveNumber(double value, String fieldName) throws InvalidInputException {
        if (value <= 0) {
            throw new InvalidInputException(fieldName + " must be a positive number.");
        }
    }

    //Validates an ISBN (simplified validation).

    public static void validateISBN(String isbn) throws InvalidInputException {
        validateNotEmpty(isbn, "ISBN");

        // Remove dashes and spaces
        String cleanIsbn = isbn.replaceAll("[\\s-]", "");

        // ISBN-10 has 10 characters, ISBN-13 has 13 characters
        if (cleanIsbn.length() != 10 && cleanIsbn.length() != 13) {
            throw new InvalidInputException("ISBN must be 10 or 13 characters long.");
        }
    }
}
