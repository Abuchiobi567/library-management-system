package com.library.repository;

import com.library.model.Member;
import com.library.model.StudentMember;
import com.library.model.StaffMember;
import com.library.util.IdGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MemberRepository Class
 * This class is responsible for saving and loading member data to/from a file.
 * It handles both StudentMember and StaffMember types.*/
public class MemberRepository {

    // ============ INSTANCE VARIABLES ============

    /** The path to the file where members are stored */
    private String filePath;

    /** The list of all members (stored in memory for quick access) */
    private List<Member> members;

    // ============ CONSTRUCTOR ============

    // Constructor that sets up the repository with a file path.

    public MemberRepository(String filePath) {
        this.filePath = filePath;
        this.members = new ArrayList<>();
    }

    // ============ CRUD OPERATIONS ============

    // Adds a new member to the repository.

    public void addMember(Member member) {
        members.add(member);
        saveToFile();
    }

    // Gets all members from the repository.

    public List<Member> getAllMembers() {
        return members;
    }

    // Finds a member by their ID.

    public Member findById(String memberId) {
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getMemberId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }

    // Updates an existing member in the repository.

    public boolean updateMember(Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getMemberId().equals(updatedMember.getMemberId())) {
                members.set(i, updatedMember);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // Removes a member from the repository.

    public boolean deleteMember(String memberId) {
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getMemberId().equals(memberId)) {
                members.remove(i);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // ============ SEARCH METHODS ============

    // Searches for members by name (partial match).

    public List<Member> searchByName(String name) {
        List<Member> results = new ArrayList<>();
        String searchTerm = name.toLowerCase();

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getName().toLowerCase().contains(searchTerm)) {
                results.add(member);
            }
        }
        return results;
    }

    // Gets all student members.

    public List<Member> getStudentMembers() {
        List<Member> students = new ArrayList<>();

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member instanceof StudentMember) {
                students.add(member);
            }
        }
        return students;
    }

    /**
     * Gets all staff members.
     *
     * @return A list of all staff members
     */
    public List<Member> getStaffMembers() {
        List<Member> staff = new ArrayList<>();

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member instanceof StaffMember) {
                staff.add(member);
            }
        }
        return staff;
    }

    // ============ FILE OPERATIONS ============

    /**Saves all members to the file.*/
    public void saveToFile() {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(filePath));

            for (int i = 0; i < members.size(); i++) {
                Member member = members.get(i);
                writer.write(member.toDataString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving members to file: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error closing file writer: " + e.getMessage());
                }
            }
        }
    }

    /**Loads all members from the file.*/
    public void loadFromFile() {
        members.clear();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Members file not found. Starting with empty member list.");
            return;
        }

        BufferedReader reader = null;
        int highestMemberId = 0;

        try {
            reader = new BufferedReader(new FileReader(filePath));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Determine the type of member
                if (line.startsWith("STUDENT,")) {
                    StudentMember student = new StudentMember();
                    student.fromDataString(line);
                    members.add(student);

                    int idNumber = extractIdNumber(student.getMemberId());
                    if (idNumber > highestMemberId) {
                        highestMemberId = idNumber;
                    }

                } else if (line.startsWith("STAFF,")) {
                    StaffMember staff = new StaffMember();
                    staff.fromDataString(line);
                    members.add(staff);

                    int idNumber = extractIdNumber(staff.getMemberId());
                    if (idNumber > highestMemberId) {
                        highestMemberId = idNumber;
                    }
                }
            }

            // Update the ID generator
            IdGenerator.setMemberCounter(highestMemberId);

        } catch (IOException e) {
            System.out.println("Error loading members from file: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing file reader: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Extracts the number part from a member ID.
     * For example, "MEM005" returns 5.*/
    private int extractIdNumber(String id) {
        try {
            String numberPart = id.replace("MEM", "");
            return Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Gets the total count of members.
     *
     * @return The number of members in the repository
     */
    public int getMemberCount() {
        return members.size();
    }
}
