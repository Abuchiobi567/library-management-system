package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidInputException;
import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.LibraryItem;
import com.library.repository.BookRepository;
import com.library.util.IdGenerator;
import com.library.util.InputValidator;

import java.util.List;

/**
 * BookService Class
 * This class contains the BUSINESS LOGIC for managing books.
 * It sits between the GUI (controller) and the data (repository).
 */
public class BookService {

    // INSTANCE VARIABLES 

    /** The repository for accessing book data */
    private BookRepository bookRepository;

    //  CONSTRUCTOR 

    /**
     * Constructor that takes a BookRepository.
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // BOOK MANAGEMENT METHODS 

    //Adds a new book to the library.

    public Book addBook(String title, String author, String isbn,
                        String genre, int publicationYear) throws LibraryException {

        // Validate all input fields
        InputValidator.validateNotEmpty(title, "Title");
        InputValidator.validateNotEmpty(author, "Author");
        InputValidator.validateISBN(isbn);
        InputValidator.validateNotEmpty(genre, "Genre");
        InputValidator.validateYear(publicationYear);

        // Generate a unique ID for the new book
        String bookId = IdGenerator.generateBookId();

        // Create the new book object
        Book newBook = new Book(bookId, title, author, isbn, genre, publicationYear);

        // Save the book to the repository
        bookRepository.addBook(newBook);

        return newBook;
    }

    // Adds a new ebook to the library.

    public EBook addEBook(String title, String author, String isbn, String genre,
                          int publicationYear, String fileFormat, double fileSizeMB,
                          String downloadLink) throws LibraryException {

        // Validate all input fields
        InputValidator.validateNotEmpty(title, "Title");
        InputValidator.validateNotEmpty(author, "Author");
        InputValidator.validateISBN(isbn);
        InputValidator.validateNotEmpty(genre, "Genre");
        InputValidator.validateYear(publicationYear);
        InputValidator.validateNotEmpty(fileFormat, "File format");
        InputValidator.validatePositiveNumber(fileSizeMB, "File size");
        InputValidator.validateNotEmpty(downloadLink, "Download link");

        // Generate a unique ID
        String ebookId = IdGenerator.generateEBookId();

        // Create the new ebook
        EBook newEBook = new EBook(ebookId, title, author, isbn, genre,
                                   publicationYear, fileFormat, fileSizeMB, downloadLink);

        // Save to repository
        bookRepository.addBook(newEBook);

        return newEBook;
    }

    //Gets all books in the library.
    public List<LibraryItem> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    //Finds a book by its ID.
    public LibraryItem getBookById(String bookId) throws BookNotFoundException {
        LibraryItem book = bookRepository.findById(bookId);

        if (book == null) {
            throw new BookNotFoundException(bookId);
        }

        return book;
    }

    // Updates an existing book's information.
    public void updateBook(String bookId, String title, String author,
                           String isbn, String genre, int publicationYear)
            throws LibraryException {

        // First, find the existing book
        LibraryItem existingItem = bookRepository.findById(bookId);
        if (existingItem == null) {
            throw new BookNotFoundException(bookId);
        }

        // Validate input
        InputValidator.validateNotEmpty(title, "Title");
        InputValidator.validateNotEmpty(author, "Author");
        InputValidator.validateISBN(isbn);
        InputValidator.validateNotEmpty(genre, "Genre");
        InputValidator.validateYear(publicationYear);

        // Update the book (only if it's a Book type)
        if (existingItem instanceof Book) {
            Book book = (Book) existingItem;
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setGenre(genre);
            book.setPublicationYear(publicationYear);

            bookRepository.updateBook(book);
        }
    }

    //Deletes a book from the library.
    public void deleteBook(String bookId) throws LibraryException {
        // Check if book exists
        LibraryItem book = bookRepository.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }

        // Check if book is currently borrowed
        if (!book.isAvailable()) {
            throw new LibraryException("Cannot delete book - it is currently borrowed.");
        }

        // Delete the book
        bookRepository.deleteBook(bookId);
    }

    //Searches for books by title.

    public List<LibraryItem> searchByTitle(String title) {
        return bookRepository.searchByTitle(title);
    }

    //Searches for books by author.

    public List<LibraryItem> searchByAuthor(String author) {
        return bookRepository.searchByAuthor(author);
    }

    // Gets all available (not borrowed) books.
    public List<LibraryItem> getAvailableBooks() {
        return bookRepository.getAvailableBooks();
    }

    //Gets the total number of books in the library.
    public int getBookCount() {
        return bookRepository.getBookCount();
    }

    // Loads books from the file.

    public void loadBooks() {
        bookRepository.loadFromFile();
    }

    // Saves books to the file.
    public void saveBooks() {
        bookRepository.saveToFile();
    }
}
