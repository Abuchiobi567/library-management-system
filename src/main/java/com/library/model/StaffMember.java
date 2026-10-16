package com.library.model;

/**
 * StaffMember Class
 *
 * This class represents a staff member of the library.
 * It EXTENDS the Member class and adds staff-specific properties
 * like employee ID and department.
 * This is an example of INHERITANCE - StaffMember "is a" Member.
 * Staff members have a higher book limit (10) and get a 15% discount.*/

public class StaffMember extends Member {

    // INSTANCE VARIABLES 
    // Properties specific to a staff member (in addition to inherited Member properties)

    /** The staff member's employee ID */
    private String employeeId;

    /** The department the staff member works in */
    private String department;

    /** The job title/position of the staff member */
    private String position;

    // CONSTANTS 
    // These are fixed values that don't change

    /** Maximum number of books a staff member can borrow */
    private static final int STAFF_BOOK_LIMIT = 10;

    /** Discount percentage for staff members */
    private static final int STAFF_DISCOUNT = 15;

    //  CONSTRUCTORS 
    /**
     * Default constructor
     * Creates an empty StaffMember with default values.
     * Needed for loading data from files.
     */
    public StaffMember() {
        super(); // Call the parent class (Member) constructor
        this.employeeId = "";
        this.department = "";
        this.position = "";
        this.maxBooksAllowed = STAFF_BOOK_LIMIT; // Set staff book limit
    }

    /**
     * Parameterized constructor
     * Creates a StaffMember with all the specified values.
     */
    public StaffMember(String memberId, String name, String email,
                       String phoneNumber, String registrationDate,
                       String employeeId, String department, String position) {
        // Call parent constructor to set common Member properties
        super(memberId, name, email, phoneNumber, registrationDate);
        this.employeeId = employeeId;
        this.department = department;
        this.position = position;
        this.maxBooksAllowed = STAFF_BOOK_LIMIT; // Set staff book limit
    }

    //  GETTER METHODS 

    /**
     * Gets the employee ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Gets the department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Gets the job position
     */
    public String getPosition() {
        return position;
    }

    //  SETTER METHODS 

    /**
     * Sets the employee ID
     *
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Sets the department name
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Sets the job position
     */
    public void setPosition(String position) {
        this.position = position;
    }

    // IMPLEMENTED ABSTRACT METHODS 
    // These methods are required by the parent class Member

    /**
     * Returns the type of this member.
     * This OVERRIDES the abstract method from Member.
     */
    @Override
    public String getMemberType() {
        return "Staff";
    }

    /**
     * Returns detailed information about this staff member.
     * This OVERRIDES the abstract method from Member.
     */
    @Override
    public String getDetails() {
        // Build a string with all the staff member information
        String details = "=== Staff Member Details ===" + "\n";
        details = details + "Member ID: " + memberId + "\n";
        details = details + "Name: " + name + "\n";
        details = details + "Email: " + email + "\n";
        details = details + "Phone: " + phoneNumber + "\n";
        details = details + "Employee ID: " + employeeId + "\n";
        details = details + "Department: " + department + "\n";
        details = details + "Position: " + position + "\n";
        details = details + "Registered: " + registrationDate + "\n";
        details = details + "Books Borrowed: " + currentBooksCount + "/" + maxBooksAllowed + "\n";
        details = details + "Discount: " + STAFF_DISCOUNT + "%" + "\n";
        return details;
    }

    /**
     * Returns the discount percentage for staff members.
     * Staff get a 15% discount.
     */
    @Override
    public int getDiscountPercentage() {
        return STAFF_DISCOUNT;
    }

    //  PERSISTABLE INTERFACE METHODS 

    /**
     * Converts the staff member data to a string format for saving to a file.
     * Format: STAFF,memberId,name,email,phone,regDate,employeeId,department,position,maxBooks,currentBooks
     */
    @Override
    public String toDataString() {
        // We add "STAFF" at the start so we know what type of member this is when loading
        String data = "STAFF" + ",";
        data = data + memberId + ",";
        data = data + name + ",";
        data = data + email + ",";
        data = data + phoneNumber + ",";
        data = data + registrationDate + ",";
        data = data + employeeId + ",";
        data = data + department + ",";
        data = data + position + ",";
        data = data + maxBooksAllowed + ",";
        data = data + currentBooksCount;
        return data;
    }

    /**
     * Loads staff member data from a comma-separated string.
     * This is the reverse of toDataString()
     */
    @Override
    public void fromDataString(String data) {
        // Split the string by commas to get individual values
        String[] parts = data.split(",");

        // Check if we have enough parts (should be 11 for a StaffMember)
        if (parts.length >= 11) {
            // parts[0] is "STAFF" - we skip it
            this.memberId = parts[1];
            this.name = parts[2];
            this.email = parts[3];
            this.phoneNumber = parts[4];
            this.registrationDate = parts[5];
            this.employeeId = parts[6];
            this.department = parts[7];
            this.position = parts[8];
            this.maxBooksAllowed = Integer.parseInt(parts[9]);
            this.currentBooksCount = Integer.parseInt(parts[10]);
        }
    }

    /**
     * Returns a simple string representation of this staff member.
     */
    @Override
    public String toString() {
        return name + " (" + employeeId + ") - " + department + ", " + position;
    }
}
