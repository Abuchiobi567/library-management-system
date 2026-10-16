package com.library.repository;

import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.LibraryItem;
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
 * BookRepository Class
 *
 * This class is responsible for saving and loading book data to/from a file.
 * It acts as a DATA ACCESS LAYER - it handles all the file operations so that
 * other parts of the application don't need to worry about how data is stored.
 * This class uses:
 * - ArrayList to store books in memory
 * - BufferedWriter to save books to a file
 * - BufferedReader to load books from a file
 */
public class BookRepository {

    // INSTANCE VARIABLES 

    /** The path to the file where books are stored */
    private String filePath;

    /** The list of all books (stored in memory for quick access) */
    private List<LibraryItem> books;

    //  CONSTRUCTOR 

    /** Constructor that sets up the repository with a file path.*/
    public BookRepository(String filePath) {
        this.filePath = filePath;
        this.books = new ArrayList<>(); // Create an empty list to store books
    }

    //  CRUD OPERATIONS 
    // CRUD = Create, Read, Update, Delete

    /**
     * Adds a new book to the repository.
     * This is the CREATE operation.
    */
    public void addBook(LibraryItem book) {
        books.add(book);
        saveToFile(); // Save changes to file immediately
    }

    /**
     * Gets all books from the repository.
     * This is the READ operation (read all).
     */
    public List<LibraryItem> getAllBooks() {
        return books;
    }

    /**
     * Finds a book by its ID.
     * This is the READ operation (read one).
     */
    public LibraryItem findById(String bookId) {
        // Loop through all books to find the one with matching ID
        for (int i = 0; i < books.size(); i++) {
            LibraryItem book = books.get(i);
            if (book.getItemId().equals(bookId)) {
                return book; // Found it!
            }
        }
        return null; // Book not found
    }

    /**
     * Updates an existing book in the repository.
     * This is the UPDATE operation.
     */
    public boolean updateBook(LibraryItem updatedBook) {
        // Find the book and replace it
        for (int i = 0; i < books.size(); i++) {
            LibraryItem book = books.get(i);
            if (book.getItemId().equals(updatedBook.getItemId())) {
                books.set(i, updatedBook); // Replace with updated book
                saveToFile(); // Save changes to file
                return true;
            }
        }
        return false; // Book not found
    }

    /**
     * Removes a book from the repository.
     * This is the DELETE operation.
     */
    public boolean deleteBook(String bookId) {
        // Find and remove the book
        for (int i = 0; i < books.size(); i++) {
            LibraryItem book = books.get(i);
            if (book.getItemId().equals(bookId)) {
                books.remove(i); // Remove the book
                saveToFile(); // Save changes to file
                return true;
            }
        }
        return false; // Book not found
    }

    // ============ SEARCH METHODS ============

    /**Searches for books by title (partial match).*/
    public List<LibraryItem> searchByTitle(String title) {
        List<LibraryItem> results = new ArrayList<>();
        String searchTerm = title.toLowerCase(); // Convert to lowercase for case-insensitive search

        for (int i = 0; i < books.size(); i++) {
            LibraryItem book = books.get(i);
            // Check if book title contains the search term
            if (book.getTitle().toLowerCase().contains(searchTerm)) {
                results.add(book);
            }
        }
        return results;
    }

    /**Searches for books by author (partial match)*/
    public List<LibraryItem> searchByAuthor(String author) {
        List<LibraryItem> results = new ArrayList<>();
        String searchTerm = author.toLowerCase();

        for (int i = 0; i < books.size(); i++) {
            LibraryItem item = books.get(i);
            // Only Books and EBooks have authors
            if (item instanceof Book) {
                Book book = (Book) item; // Cast to Book to access getAuthor()
                if (book.getAuthor().toLowerCase().contains(searchTerm)) {
                    results.add(book);
                }
            }
        }
        return results;
    }

    /**Gets all available books (not currently borrowed).*/
    public List<LibraryItem> getAvailableBooks() {
        List<LibraryItem> availableBooks = new ArrayList<>();

        for (int i = 0; i < books.size(); i++) {
            LibraryItem book = books.get(i);
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    // FILE OPERATIONS 

    /**
     * Saves all books to the file.
     * Each book is written on a separate line using its toDataString() method.
     */
    public void saveToFile() {
        BufferedWriter writer = null;

        try {
            // Create the file writer
            writer = new BufferedWriter(new FileWriter(filePath));

            // Write each book to the file
            for (int i = 0; i < books.size(); i++) {
                LibraryItem book = books.get(i);
                writer.write(book.toDataString()); // Convert book to string
                writer.newLine(); // Add a new line after each book
            }

        } catch (IOException e) {
            // Handle any file writing errors
            System.out.println("Error saving books to file: " + e.getMessage());
        } finally {
            // Always close the writer
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error closing file writer: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Loads all books from the file.
     * Each line in the file represents one book.
     */
    public void loadFromFile() {
        // Clear the current list before loading
        books.clear();

        // Check if file exists
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Books file not found. Starting with empty book list.");
            return;
        }

        BufferedReader reader = null;
        int highestBookId = 0;
        int highestEBookId = 0;

        try {
            // Create the file reader
            reader = new BufferedReader(new FileReader(filePath));

            String line;
            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Determine the type of item based on the first part of the line
                if (line.startsWith("BOOK,")) {
                    // It's a regular Book
                    Book book = new Book();
                    book.fromDataString(line);
                    books.add(book);

                    // Update the counter to avoid ID conflicts
                    int idNumber = extractIdNumber(book.getItemId(), "BK");
                    if (idNumber > highestBookId) {
                        highestBookId = idNumber;
                    }

                } else if (line.startsWith("EBOOK,")) {
                    // It's an EBook
                    EBook ebook = new EBook();
                    ebook.fromDataString(line);
                    books.add(ebook);

                    // Update the counter
                    int idNumber = extractIdNumber(ebook.getItemId(), "EB");
                    if (idNumber > highestEBookId) {
                        highestEBookId = idNumber;
                    }
                }
            }

            // Update the ID generators so new IDs don't conflict
            IdGenerator.setBookCounter(highestBookId);
            IdGenerator.setEBookCounter(highestEBookId);

        } catch (IOException e) {
            System.out.println("Error loading books from file: " + e.getMessage());
        } finally {
            // Always close the reader
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
     * Extracts the number part from an ID string.
     * For example, "BK005" returns 5.*/
    private int extractIdNumber(String id, String prefix) {
        try {
            String numberPart = id.replace(prefix, "");
            return Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**Gets the total count of books.*/
    public int getBookCount() {
        return books.size();
    }
}
