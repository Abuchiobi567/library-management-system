package com.library.service;

import com.library.exception.BookNotAvailableException;
import com.library.exception.BookNotFoundException;
import com.library.exception.BorrowingLimitExceededException;
import com.library.exception.LibraryException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Book;
import com.library.model.LibraryItem;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.MemberRepository;
import com.library.util.IdGenerator;

import java.util.List;

/**
 * LoanService Class
 * This class contains the BUSINESS LOGIC for managing loans (borrowing and returning books).
 * It coordinates between books, members, and loans to handle the borrowing process.
 */
public class LoanService {

    // ============ INSTANCE VARIABLES ============

    /** Repository for loan data */
    private LoanRepository loanRepository;

    /** Repository for book data */
    private BookRepository bookRepository;

    /** Repository for member data */
    private MemberRepository memberRepository;

    // ============ CONSTRUCTOR ============

    //Constructor that takes all required repositories.
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository,
                       MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    // ============ BORROWING METHODS ============

    /**
     * Borrows a book for a member.
     * This method checks all business rules before creating the loan.
     */
    public Loan borrowBook(String bookId, String memberId) throws LibraryException {

        // Step 1: Check if the book exists
        LibraryItem book = bookRepository.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }

        // Step 2: Check if the book is available
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(bookId);
        }

        // Step 3: Check if the member exists
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            throw new MemberNotFoundException(memberId);
        }

        // Step 4: Check if the member has reached their borrowing limit
        if (!member.canBorrowMore()) {
            throw new BorrowingLimitExceededException(memberId, member.getMaxBooksAllowed());
        }

        // All checks passed - create the loan

        // Step 5: Generate a new loan ID
        String loanId = IdGenerator.generateLoanId();

        // Step 6: Create the loan record
        Loan newLoan = new Loan(loanId, bookId, memberId);

        // Step 7: Update the book status to not available
        book.setAvailable(false);
        bookRepository.updateBook(book);

        // Step 8: Update the member's borrowed book count
        member.incrementBooksCount();
        memberRepository.updateMember(member);

        // Step 9: Save the loan
        loanRepository.addLoan(newLoan);

        return newLoan;
    }

    /**
     * Returns a borrowed book.
     * This method finds the active loan and marks it as returned.
     */
    public Loan returnBook(String bookId) throws LibraryException {

        // Step 1: Check if the book exists
        LibraryItem book = bookRepository.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }

        // Step 2: Check if the book is actually borrowed
        if (book.isAvailable()) {
            throw new LibraryException("Book " + bookId + " is not currently borrowed.");
        }

        // Step 3: Find the active loan for this book
        Loan activeLoan = loanRepository.findActiveLoanByBookId(bookId);
        if (activeLoan == null) {
            throw new LibraryException("No active loan found for book " + bookId);
        }

        // Step 4: Get the member who borrowed the book
        Member member = memberRepository.findById(activeLoan.getMemberId());

        // Step 5: Mark the loan as returned
        activeLoan.returnBook();
        loanRepository.updateLoan(activeLoan);

        // Step 6: Update the book status to available
        book.setAvailable(true);
        bookRepository.updateBook(book);

        // Step 7: Update the member's borrowed book count
        if (member != null) {
            member.decrementBooksCount();
            memberRepository.updateMember(member);
        }

        return activeLoan;
    }

    // ============ LOAN QUERY METHODS ============

    // Gets all loans.

    public List<Loan> getAllLoans() {
        return loanRepository.getAllLoans();
    }

    // Gets all active (not returned) loans.
    public List<Loan> getActiveLoans() {
        return loanRepository.getActiveLoans();
    }

    // Gets all overdue loans.

    public List<Loan> getOverdueLoans() {
        return loanRepository.getOverdueLoans();
    }

    // Gets all loans for a specific member.
    public List<Loan> getLoansByMember(String memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    //Gets all active loans for a specific member.

    public List<Loan> getActiveLoansByMember(String memberId) {
        return loanRepository.getActiveLoansByMemberId(memberId);
    }

    //Gets the loan history for a specific book.

    public List<Loan> getLoanHistoryByBook(String bookId) {
        return loanRepository.findByBookId(bookId);
    }

    //Finds a loan by its ID.
    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId);
    }

    // ============ REPORTING METHODS ============

    //Calculates the total fines for all overdue loans.

    public double calculateTotalFines() {
        List<Loan> overdueLoans = loanRepository.getOverdueLoans();
        double totalFines = 0.0;

        for (int i = 0; i < overdueLoans.size(); i++) {
            Loan loan = overdueLoans.get(i);
            totalFines = totalFines + loan.calculateFine();
        }

        return totalFines;
    }

    //Calculates the fine for a specific member's overdue books.

    public double calculateMemberFines(String memberId) {
        List<Loan> memberLoans = loanRepository.getActiveLoansByMemberId(memberId);
        double totalFines = 0.0;

        for (int i = 0; i < memberLoans.size(); i++) {
            Loan loan = memberLoans.get(i);
            if (loan.isOverdue()) {
                totalFines = totalFines + loan.calculateFine();
            }
        }

        return totalFines;
    }

    //Gets the total number of loans.
    public int getTotalLoanCount() {
        return loanRepository.getLoanCount();
    }

    //Gets the number of active loans.

    public int getActiveLoanCount() {
        return loanRepository.getActiveLoanCount();
    }

    //Gets the number of overdue loans.

    public int getOverdueLoanCount() {
        return loanRepository.getOverdueLoans().size();
    }

    // ============ UTILITY METHODS ============

    //Loads loans from the file.
    public void loadLoans() {
        loanRepository.loadFromFile();
    }

    //Saves loans to the file.

    public void saveLoans() {
        loanRepository.saveToFile();
    }

    //Gets the book details for a loan.

    public LibraryItem getBookForLoan(Loan loan) {
        return bookRepository.findById(loan.getBookId());
    }

    // Gets the member details for a loan.

    public Member getMemberForLoan(Loan loan) {
        return memberRepository.findById(loan.getMemberId());
    }
}
