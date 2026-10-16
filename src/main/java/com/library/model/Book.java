package com.library.model;

/**
 * Book Class
 * This class represents a physical book in the library.
 * It EXTENDS the LibraryItem class, which means it INHERITS all the
 * properties and methods from LibraryItem (like itemId, title, isAvailable).*/
public class Book extends LibraryItem {

    // INSTANCE VARIABLES
    // These are properties specific to a Book (in addition to inherited ones)

    /** The author of the book */
    private String author;

    /** The ISBN (International Standard Book Number) - unique identifier for books */
    private String isbn;

    /** The genre/category of the book (e.g., Fiction, Science, History) */
    private String genre;

    /** The year the book was published */
    private int publicationYear;

    // CONSTRUCTORS 

    /**
     * Default constructor
     * Creates an empty Book with default values.
     * Needed for loading data from files.
     */
    public Book() {
        super(); // Call the parent class (LibraryItem) constructor
        this.author = "";
        this.isbn = "";
        this.genre = "";
        this.publicationYear = 0;
    }

    /**
     * Parameterized constructor
     * Creates a Book with all the specified values.*/

    public Book(String itemId, String title, String author, String isbn,
                String genre, int publicationYear) {
        super(itemId, title); // Call parent constructor to set itemId and title
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.publicationYear = publicationYear;
    }

    // GETTER METHODS 

    /**
     * Gets the author's name */

    public String getAuthor() {
        return author;
    }

    /**Gets the ISBN */

    public String getIsbn() {
        return isbn;
    }

    // Gets the genre/category

    public String getGenre() {
        return genre;
    }

    // Gets the publication year

    public int getPublicationYear() {
        return publicationYear;
    }

    // SETTER METHODS

    // Sets the author's name

    public void setAuthor(String author) {
        this.author = author;
    }

    // Sets the ISBN

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // Sets the genre/category

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Sets the publication year

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    // IMPLEMENTED ABSTRACT METHODS 
    // These methods are required by the parent class LibraryItem

    /**
     * Returns the type of this item.
     * This OVERRIDES the abstract method from LibraryItem.*/

    @Override
    public String getItemType() {
        return "Book";
    }

    /**
     * Returns detailed information about this book.
     * This OVERRIDES the abstract method from LibraryItem.*/

    @Override
    public String getDetails() {
        // Build a string with all the book information
        String details = "=== Book Details ===" + "\n";
        details = details + "ID: " + itemId + "\n";
        details = details + "Title: " + title + "\n";
        details = details + "Author: " + author + "\n";
        details = details + "ISBN: " + isbn + "\n";
        details = details + "Genre: " + genre + "\n";
        details = details + "Year: " + publicationYear + "\n";
        details = details + "Status: " + (isAvailable ? "Available" : "Borrowed") + "\n";
        return details;
    }

    // PERSISTABLE INTERFACE METHODS 
    // These methods allow the book to be saved to and loaded from a file

    /** Converts the book data to a string format for saving to a file.*/


    @Override
    public String toDataString() {
        // We add "BOOK" at the start so we know what type of item this is when loading
        String data = "BOOK" + ",";
        data = data + itemId + ",";
        data = data + title + ",";
        data = data + author + ",";
        data = data + isbn + ",";
        data = data + genre + ",";
        data = data + publicationYear + ",";
        data = data + isAvailable;
        return data;
    }

    /**
     * Loads book data from a comma-separated string.
     * This is the reverse of toDataString().*/

    @Override
    public void fromDataString(String data) {
        // Split the string by commas to get individual values
        String[] parts = data.split(",");

        // Check if we have enough parts (should be 8 for a Book)
        if (parts.length >= 8) {
            // parts[0] is "BOOK" - we skip it
            this.itemId = parts[1];
            this.title = parts[2];
            this.author = parts[3];
            this.isbn = parts[4];
            this.genre = parts[5];
            this.publicationYear = Integer.parseInt(parts[6]); // Convert string to int
            this.isAvailable = Boolean.parseBoolean(parts[7]); // Convert string to boolean
        }
    }

    /**
     * Returns a simple string representation of this book.
     * Useful for displaying in lists.*/

    @Override
    public String toString() {
        return title + " by " + author + " (" + (isAvailable ? "Available" : "Borrowed") + ")";
    }
}
