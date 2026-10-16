package com.library.model;

/**
 * Persistable Interface
 * This interface defines a contract for objects that can be saved to
 * and loaded from a file. Any class that implements this interface
 * must provide methods to convert its data to a string format for
 * saving, and to load data from a string format.
 */
public interface Persistable {

    /**
     * Converts the object's data to a string format that can be saved to a file.
     * Each class will decide how to format its own data.
     */
    String toDataString();

    /**
     * Loads data from a string and populates the object's fields.
     * This is the reverse of toDataString() method.
     */
    void fromDataString(String data);
}
