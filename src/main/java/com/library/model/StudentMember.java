package com.library.model;

/**
 * StudentMember Class
 *
 * This class represents a student member of the library.
 * It EXTENDS the Member class and adds student-specific properties
 * like student ID, course, and year of study.
 * This is an example of INHERITANCE - StudentMember "is a" Member.
 * Students have a limit of 5 books and get a 10% discount.
 */
public class StudentMember extends Member {

    // INSTANCE VARIABLES 
    // Properties specific to a student (in addition to inherited Member properties)

    /** The student's university ID number */
    private String studentId;

    /** The course the student is enrolled in */
    private String course;

    /** The current year of study (1, 2, 3, etc.) */
    private int yearOfStudy;

    // CONSTANTS 
    // These are fixed values that don't change

    /** Maximum number of books a student can borrow */
    private static final int STUDENT_BOOK_LIMIT = 5;

    /** Discount percentage for students */
    private static final int STUDENT_DISCOUNT = 10;

    //  CONSTRUCTORS 
    /**
     * Default constructor
     * Creates an empty StudentMember with default values.
     * Needed for loading data from files.
     */
    public StudentMember() {
        super(); // Call the parent class (Member) constructor
        this.studentId = "";
        this.course = "";
        this.yearOfStudy = 1;
        this.maxBooksAllowed = STUDENT_BOOK_LIMIT; // Set student book limit
    }

    /**
     * Parameterized constructor
     * Creates a StudentMember with all the specified values.
     */
    public StudentMember(String memberId, String name, String email,
                         String phoneNumber, String registrationDate,
                         String studentId, String course, int yearOfStudy) {
        // Call parent constructor to set common Member properties
        super(memberId, name, email, phoneNumber, registrationDate);
        this.studentId = studentId;
        this.course = course;
        this.yearOfStudy = yearOfStudy;
        this.maxBooksAllowed = STUDENT_BOOK_LIMIT; // Set student book limit
    }

    // GETTER METHODS 

    /**
     * Gets the student's university ID
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Gets the course name
     */
    public String getCourse() {
        return course;
    }

    /**
     * Gets the year of study
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    // SETTER METHODS

    /**
     * Sets the student's university ID
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Sets the course name
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Sets the year of study
     */
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    // IMPLEMENTED ABSTRACT METHODS
    // These methods are required by the parent class Member

    /**
     * Returns the type of this member.
     * This OVERRIDES the abstract method from Member.
     */
    @Override
    public String getMemberType() {
        return "Student";
    }

    /**
     * Returns detailed information about this student member.
     * This OVERRIDES the abstract method from Member
     */
    @Override
    public String getDetails() {
        // Build a string with all the student information
        String details = "=== Student Member Details ===" + "\n";
        details = details + "Member ID: " + memberId + "\n";
        details = details + "Name: " + name + "\n";
        details = details + "Email: " + email + "\n";
        details = details + "Phone: " + phoneNumber + "\n";
        details = details + "Student ID: " + studentId + "\n";
        details = details + "Course: " + course + "\n";
        details = details + "Year: " + yearOfStudy + "\n";
        details = details + "Registered: " + registrationDate + "\n";
        details = details + "Books Borrowed: " + currentBooksCount + "/" + maxBooksAllowed + "\n";
        details = details + "Discount: " + STUDENT_DISCOUNT + "%" + "\n";
        return details;
    }

    /**
     * Returns the discount percentage for students.
     * Students get a 10% discount.
     */
    @Override
    public int getDiscountPercentage() {
        return STUDENT_DISCOUNT;
    }

    // PERSISTABLE INTERFACE METHODS 

    /**
     * Converts the student member data to a string format for saving to a file.
     * Format: STUDENT,memberId,name,email,phone,regDate,studentId,course,year,maxBooks,currentBooks
     */
    @Override
    public String toDataString() {
        // We add "STUDENT" at the start so we know what type of member this is when loading
        String data = "STUDENT" + ",";
        data = data + memberId + ",";
        data = data + name + ",";
        data = data + email + ",";
        data = data + phoneNumber + ",";
        data = data + registrationDate + ",";
        data = data + studentId + ",";
        data = data + course + ",";
        data = data + yearOfStudy + ",";
        data = data + maxBooksAllowed + ",";
        data = data + currentBooksCount;
        return data;
    }

    /**
     * Loads student member data from a comma-separated string.
     * This is the reverse of toDataString().
     */
    @Override
    public void fromDataString(String data) {
        // Split the string by commas to get individual values
        String[] parts = data.split(",");

        // Check if we have enough parts (should be 11 for a StudentMember)
        if (parts.length >= 11) {
            // parts[0] is "STUDENT" - we skip it
            this.memberId = parts[1];
            this.name = parts[2];
            this.email = parts[3];
            this.phoneNumber = parts[4];
            this.registrationDate = parts[5];
            this.studentId = parts[6];
            this.course = parts[7];
            this.yearOfStudy = Integer.parseInt(parts[8]);
            this.maxBooksAllowed = Integer.parseInt(parts[9]);
            this.currentBooksCount = Integer.parseInt(parts[10]);
        }
    }

    /**
     * Returns a simple string representation of this student member.
     */
    @Override
    public String toString() {
        return name + " (" + studentId + ") - " + course + ", Year " + yearOfStudy;
    }
}
