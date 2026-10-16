package com.library.service;

import com.library.exception.InvalidInputException;
import com.library.exception.LibraryException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Member;
import com.library.model.StudentMember;
import com.library.model.StaffMember;
import com.library.repository.MemberRepository;
import com.library.util.IdGenerator;
import com.library.util.InputValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * MemberService Class
 * This class contains the BUSINESS LOGIC for managing library members.
 * It handles registration of new members, updates, and member queries.
 */
public class MemberService {

    // ============ INSTANCE VARIABLES ============

    /** The repository for accessing member data */
    private MemberRepository memberRepository;

    /** Date format for registration dates */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ============ CONSTRUCTOR ============

    // Constructor that takes a MemberRepository.

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // ============ MEMBER REGISTRATION METHODS ============

    // Registers a new student member.

    public StudentMember registerStudent(String name, String email, String phoneNumber,
                                         String studentId, String course, int yearOfStudy)
            throws LibraryException {

        // Validate all input fields
        InputValidator.validateNotEmpty(name, "Name");
        InputValidator.validateEmail(email);
        InputValidator.validatePhoneNumber(phoneNumber);
        InputValidator.validateNotEmpty(studentId, "Student ID");
        InputValidator.validateNotEmpty(course, "Course");
        InputValidator.validatePositiveNumber(yearOfStudy, "Year of study");

        // Validate year of study is reasonable (1-7 for most courses)
        if (yearOfStudy > 7) {
            throw new InvalidInputException("Year of study cannot be greater than 7.");
        }

        // Generate a unique member ID
        String memberId = IdGenerator.generateMemberId();

        // Get today's date as registration date
        String registrationDate = LocalDate.now().format(DATE_FORMAT);

        // Create the new student member
        StudentMember newStudent = new StudentMember(memberId, name, email, phoneNumber,
                                                     registrationDate, studentId, course, yearOfStudy);

        // Save to repository
        memberRepository.addMember(newStudent);

        return newStudent;
    }

    // Registers a new staff member.

    public StaffMember registerStaff(String name, String email, String phoneNumber,
                                     String employeeId, String department, String position)
            throws LibraryException {

        // Validate all input fields
        InputValidator.validateNotEmpty(name, "Name");
        InputValidator.validateEmail(email);
        InputValidator.validatePhoneNumber(phoneNumber);
        InputValidator.validateNotEmpty(employeeId, "Employee ID");
        InputValidator.validateNotEmpty(department, "Department");
        InputValidator.validateNotEmpty(position, "Position");

        // Generate a unique member ID
        String memberId = IdGenerator.generateMemberId();

        // Get today's date as registration date
        String registrationDate = LocalDate.now().format(DATE_FORMAT);

        // Create the new staff member
        StaffMember newStaff = new StaffMember(memberId, name, email, phoneNumber,
                                               registrationDate, employeeId, department, position);

        // Save to repository
        memberRepository.addMember(newStaff);

        return newStaff;
    }

    // ============ MEMBER QUERY METHODS ============

    // Gets all members in the library.

    public List<Member> getAllMembers() {
        return memberRepository.getAllMembers();
    }

    // Finds a member by their ID.

    public Member getMemberById(String memberId) throws MemberNotFoundException {
        Member member = memberRepository.findById(memberId);

        if (member == null) {
            throw new MemberNotFoundException(memberId);
        }

        return member;
    }

    // Searches for members by name.

    public List<Member> searchByName(String name) {
        return memberRepository.searchByName(name);
    }

    // Gets all student members.

    public List<Member> getStudentMembers() {
        return memberRepository.getStudentMembers();
    }

    // Gets all staff members.

    public List<Member> getStaffMembers() {
        return memberRepository.getStaffMembers();
    }

    // ============ MEMBER UPDATE METHODS ============

    // Updates a student member's information.

    public void updateStudentMember(String memberId, String name, String email,
                                    String phoneNumber, String course, int yearOfStudy)
            throws LibraryException {

        // Find the existing member
        Member existingMember = memberRepository.findById(memberId);
        if (existingMember == null) {
            throw new MemberNotFoundException(memberId);
        }

        // Check if it's a student member
        if (!(existingMember instanceof StudentMember)) {
            throw new LibraryException("Member " + memberId + " is not a student member.");
        }

        // Validate input
        InputValidator.validateNotEmpty(name, "Name");
        InputValidator.validateEmail(email);
        InputValidator.validatePhoneNumber(phoneNumber);
        InputValidator.validateNotEmpty(course, "Course");
        InputValidator.validatePositiveNumber(yearOfStudy, "Year of study");

        // Update the student member
        StudentMember student = (StudentMember) existingMember;
        student.setName(name);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setCourse(course);
        student.setYearOfStudy(yearOfStudy);

        memberRepository.updateMember(student);
    }

    // Updates a staff member's information.

    public void updateStaffMember(String memberId, String name, String email,
                                  String phoneNumber, String department, String position)
            throws LibraryException {

        // Find the existing member
        Member existingMember = memberRepository.findById(memberId);
        if (existingMember == null) {
            throw new MemberNotFoundException(memberId);
        }

        // Check if it's a staff member
        if (!(existingMember instanceof StaffMember)) {
            throw new LibraryException("Member " + memberId + " is not a staff member.");
        }

        // Validate input
        InputValidator.validateNotEmpty(name, "Name");
        InputValidator.validateEmail(email);
        InputValidator.validatePhoneNumber(phoneNumber);
        InputValidator.validateNotEmpty(department, "Department");
        InputValidator.validateNotEmpty(position, "Position");

        // Update the staff member
        StaffMember staff = (StaffMember) existingMember;
        staff.setName(name);
        staff.setEmail(email);
        staff.setPhoneNumber(phoneNumber);
        staff.setDepartment(department);
        staff.setPosition(position);

        memberRepository.updateMember(staff);
    }

    // Deletes a member from the library.
    public void deleteMember(String memberId) throws LibraryException {
        // Check if member exists
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            throw new MemberNotFoundException(memberId);
        }

        // Check if member has borrowed books
        if (member.getCurrentBooksCount() > 0) {
            throw new LibraryException("Cannot delete member - they have " +
                    member.getCurrentBooksCount() + " book(s) currently borrowed.");
        }

        // Delete the member
        memberRepository.deleteMember(memberId);
    }

    // ============ UTILITY METHODS ============

    // Gets the total number of members.

    public int getMemberCount() {
        return memberRepository.getMemberCount();
    }

    //Loads members from the file.

    public void loadMembers() {
        memberRepository.loadFromFile();
    }

    //Saves members to the file.

    public void saveMembers() {
        memberRepository.saveToFile();
    }
}
