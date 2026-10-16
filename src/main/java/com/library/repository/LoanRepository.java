package com.library.repository;

import com.library.model.Loan;
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
 * LoanRepository Class
 * This class is responsible for saving and loading loan data to/from a file.
 * It tracks all borrowing records - who borrowed what book and when.
 */
public class LoanRepository {

    // ============ INSTANCE VARIABLES ============

    /** The path to the file where loans are stored */
    private String filePath;

    /** The list of all loans (stored in memory for quick access) */
    private List<Loan> loans;

    // ============ CONSTRUCTOR ============

    /** Constructor that sets up the repository with a file path.*/
    public LoanRepository(String filePath) {
        this.filePath = filePath;
        this.loans = new ArrayList<>();
    }

    // ============ CRUD OPERATIONS ============

    // Adds a new loan to the repository

    public void addLoan(Loan loan) {
        loans.add(loan);
        saveToFile();
    }

    /**
     * Gets all loans from the repository.
     *
     * @return A list of all loans
     */
    public List<Loan> getAllLoans() {
        return loans;
    }

    /**
     * Finds a loan by its ID.
     *
     * @param loanId The ID of the loan to find
     * @return The loan if found, or null if not found
     */
    public Loan findById(String loanId) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.getLoanId().equals(loanId)) {
                return loan;
            }
        }
        return null;
    }

    // Updates an existing loan in the repository.

    public boolean updateLoan(Loan updatedLoan) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.getLoanId().equals(updatedLoan.getLoanId())) {
                loans.set(i, updatedLoan);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // Removes a loan from the repository.

    public boolean deleteLoan(String loanId) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.getLoanId().equals(loanId)) {
                loans.remove(i);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // ============ SEARCH AND FILTER METHODS ============

    // Finds all loans for a specific member.

    public List<Loan> findByMemberId(String memberId) {
        List<Loan> memberLoans = new ArrayList<>();

        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.getMemberId().equals(memberId)) {
                memberLoans.add(loan);
            }
        }
        return memberLoans;
    }

    // Finds all loans for a specific book.

    public List<Loan> findByBookId(String bookId) {
        List<Loan> bookLoans = new ArrayList<>();

        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.getBookId().equals(bookId)) {
                bookLoans.add(loan);
            }
        }
        return bookLoans;
    }

    /**
     * Gets all active (not returned) loans.
     * @return A list of active loans
     */
    public List<Loan> getActiveLoans() {
        List<Loan> activeLoans = new ArrayList<>();

        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.isActive()) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    // Gets all overdue loans.

    public List<Loan> getOverdueLoans() {
        List<Loan> overdueLoans = new ArrayList<>();

        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.isOverdue()) {
                overdueLoans.add(loan);
            }
        }
        return overdueLoans;
    }

    /**
     * Finds the active loan for a specific book.
     * A book should only have one active loan at a time.*/
    public Loan findActiveLoanByBookId(String bookId) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.getBookId().equals(bookId) && loan.isActive()) {
                return loan;
            }
        }
        return null;
    }

    /**Gets all active loans for a specific member.*/
    public List<Loan> getActiveLoansByMemberId(String memberId) {
        List<Loan> activeLoans = new ArrayList<>();

        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            if (loan.getMemberId().equals(memberId) && loan.isActive()) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    // ============ FILE OPERATIONS ============

    /**
     * Saves all loans to the file.
     */
    public void saveToFile() {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(filePath));

            for (int i = 0; i < loans.size(); i++) {
                Loan loan = loans.get(i);
                writer.write(loan.toDataString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving loans to file: " + e.getMessage());
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

    /**Loads all loans from the file.*/
    public void loadFromFile() {
        loans.clear();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Loans file not found. Starting with empty loan list.");
            return;
        }

        BufferedReader reader = null;
        int highestLoanId = 0;

        try {
            reader = new BufferedReader(new FileReader(filePath));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (line.startsWith("LOAN,")) {
                    Loan loan = new Loan();
                    loan.fromDataString(line);
                    loans.add(loan);

                    int idNumber = extractIdNumber(loan.getLoanId());
                    if (idNumber > highestLoanId) {
                        highestLoanId = idNumber;
                    }
                }
            }

            // Update the ID generator
            IdGenerator.setLoanCounter(highestLoanId);

        } catch (IOException e) {
            System.out.println("Error loading loans from file: " + e.getMessage());
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

    /**Extracts the number part from a loan ID.*/
    private int extractIdNumber(String id) {
        try {
            String numberPart = id.replace("LN", "");
            return Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    //Gets the total count of loans.

    public int getLoanCount() {
        return loans.size();
    }

    // Gets the count of active loans.

    public int getActiveLoanCount() {
        int count = 0;
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).isActive()) {
                count = count + 1;
            }
        }
        return count;
    }
}
