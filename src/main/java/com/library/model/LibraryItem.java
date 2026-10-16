package com.library.model;

/**
 * LibraryItem Abstract Class
 * This is an ABSTRACT class that represents a general item in the library.
 * It contains common properties that all library items share, such as
 * an ID, title, and availability status.
 */
public abstract class LibraryItem implements Persistable {

    // INSTANCE VARIABLES 
    // These are the properties that every library item has

    /** Unique identifier for this item */
    protected String itemId;

    /** The title of this item */
    protected String title;

    /** Whether this item is available for borrowing */
    protected boolean isAvailable;

    // CONSTRUCTORS 

    /**
     * Default constructor
     * Creates an empty LibraryItem with default values.
     * This is needed for when we load data from files.
     */
    public LibraryItem() {
        this.itemId = "";
        this.title = "";
        this.isAvailable = true;
    }

    /**
     * Parameterized constructor
     * Creates a LibraryItem with the given values.*/
    public LibraryItem(String itemId, String title) {
        this.itemId = itemId;
        this.title = title;
        this.isAvailable = true; // New items are available by default
    }

    // GETTER METHODS 
    // These methods allow other classes to READ the private variables

    /**
     * Gets the item's unique ID
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Gets the item's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Checks if the item is available for borrowing
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    // SETTER METHODS
    // These methods allow other classes to CHANGE the private variables

    /**
     * Sets the item's unique ID
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * Sets the item's title
     
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the availability status of the item
     */
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    //  ABSTRACT METHODS 
    // These methods MUST be implemented by subclasses

    /**
     * Gets the type of this library item (e.g., "Book", "EBook")
     * This is an abstract method - each subclass will provide its own implementation.*/

    public abstract String getItemType();

    /**
     * Gets detailed information about this item.
     * Each subclass will format this differently based on its specific properties.*/
    public abstract String getDetails();

    // COMMON METHODS 

    /**
     * Returns a simple string representation of this item.
     */
    @Override
    public String toString() {
        return itemId + " - " + title + " (" + (isAvailable ? "Available" : "Borrowed") + ")";
    }
}
