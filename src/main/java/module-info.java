/**
 * Module Definition for Library Management System
 * This file defines the module for the Java Platform Module System (JPMS).
 * It specifies which other modules this application depends on and
 * which packages are accessible to other modules.
 */
module com.library {
    // Required JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Allow JavaFX to access our controller package
    opens com.library.controller to javafx.fxml;

    // Allow JavaFX to access our model package (for TableView)
    opens com.library.model to javafx.base;

    // Export packages that might be used by other modules
    exports com.library;
    exports com.library.model;
    exports com.library.service;
    exports com.library.repository;
    exports com.library.exception;
    exports com.library.util;
    exports com.library.controller;
}
