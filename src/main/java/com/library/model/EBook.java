package com.library.model;

/*
 * EBook Class
 * This class represents an electronic book (e-book) in the library.
 * It EXTENDS the Book class, which means it INHERITS all the properties
 * and methods from Book (and therefore also from LibraryItem).*/
public class EBook extends Book {

    // INSTANCE VARIABLES 
    // Properties specific to an EBook (in addition to inherited Book properties)

    /* The file format of the ebook (e.g., PDF, EPUB, MOBI) */
    private String fileFormat;

    /* The file size in megabytes (MB) */
    private double fileSizeMB;

    /* The URL or path where the ebook can be downloaded */
    private String downloadLink;

    // CONSTRUCTORS

    /**
     * Default constructor
     * Creates an empty EBook with default values.
     * Needed for loading data from files.
     */
    public EBook() {
        super(); // Call the parent class (Book) constructor
        this.fileFormat = "";
        this.fileSizeMB = 0.0;
        this.downloadLink = "";
    }

    /**
     * Parameterized constructor
     * Creates an EBook with all the specified values.
     */
    public EBook(String itemId, String title, String author, String isbn,
                 String genre, int publicationYear, String fileFormat,
                 double fileSizeMB, String downloadLink) {
        // Call parent constructor to set all Book properties
        super(itemId, title, author, isbn, genre, publicationYear);
        this.fileFormat = fileFormat;
        this.fileSizeMB = fileSizeMB;
        this.downloadLink = downloadLink;
    }

    // GETTER METHODS 

    //Gets the file format
     
    public String getFileFormat() {
        return fileFormat;
    }

    /**
     * Gets the file size in megabytes
     */
    public double getFileSizeMB() {
        return fileSizeMB;
    }

    /**
     * Gets the download link
     */
    public String getDownloadLink() {
        return downloadLink;
    }

    // SETTER METHODS 

    /**
     * Sets the file format
     */
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    /*
     * Sets the file size in megabytes
     */
    public void setFileSizeMB(double fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }

    /**
     * Sets the download link
     */
    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    // OVERRIDDEN METHODS
    // These methods OVERRIDE the parent class methods to provide EBook-specific behaviour

    /**
     * Returns the type of this item.
     * This OVERRIDES the method from Book class.
     * This is an example of POLYMORPHISM - same method name, different behaviour.*/
    
    public String getItemType() {
        return "EBook";
    }

    /**
     * Returns detailed information about this ebook.
     * This OVERRIDES the method from Book class.*/

    @Override
    public String getDetails() {
        // Build a string with all the ebook information
        return String.format("""
                === EBook Details ===
                ID: %s
                Title: %s
                Author: %s
                ISBN: %s
                Genre: %s
                Year: %s
                Format: %s
                Size: %.1f MB
                Download: %s
                Status: %s
                """, itemId, title, getAuthor(), getIsbn(), getGenre(), 
                getPublicationYear(), fileFormat, fileSizeMB, downloadLink,
                (isAvailable ? "Available" : "Borrowed"));
    }

    // PERSISTABLE INTERFACE METHODS 

    /**
     * Converts the ebook data to a string format for saving to a file.
     * Format: EBOOK,itemId,title,author,isbn,genre,year,isAvailable,format,size,link */

    @Override
    public String toDataString() {
        // We add "EBOOK" at the start so we know what type of item this is when loading
        String data = "EBOOK" + ",";
        data = data + itemId + ",";
        data = data + title + ",";
        data = data + getAuthor() + ",";
        data = data + getIsbn() + ",";
        data = data + getGenre() + ",";
        data = data + getPublicationYear() + ",";
        data = data + isAvailable + ",";
        data = data + fileFormat + ",";
        data = data + fileSizeMB + ",";
        data = data + downloadLink;
        return data;
    }

    /**
     * Loads ebook data from a comma-separated string.
     * This is the reverse of toDataString(). */

    @Override
    public void fromDataString(String data) {
        // Split the string by commas to get individual values
        String[] parts = data.split(",");

        // Check if we have enough parts (should be 11 for an EBook)
        if (parts.length >= 11) {
            // parts[0] is "EBOOK" - we skip it
            this.itemId = parts[1];
            this.title = parts[2];
            setAuthor(parts[3]);
            setIsbn(parts[4]);
            setGenre(parts[5]);
            setPublicationYear(Integer.parseInt(parts[6]));
            this.isAvailable = Boolean.parseBoolean(parts[7]);
            this.fileFormat = parts[8];
            this.fileSizeMB = Double.parseDouble(parts[9]);
            this.downloadLink = parts[10];
        }
    }

    /** Returns a simple string representation of this ebook.*/
    @Override
    public String toString() {
        return title + " by " + getAuthor() + " [" + fileFormat + "] (" +
               (isAvailable ? "Available" : "Borrowed") + ")";
    }
}
