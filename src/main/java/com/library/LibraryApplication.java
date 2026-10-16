package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * LibraryApplication - Main Entry Point
 * This is the main class that starts the Library Management System.
 * It extends JavaFX's Application class, which is required for all JavaFX applications.
 * The application lifecycle:
 * 1. main() method is called by Java
 * 2. launch() starts JavaFX
 * 3. start() method is called to set up the GUI
 */
public class LibraryApplication extends Application {

    /**
     * The start method is the main entry point for JavaFX applications.
     * This method is called after the application has been initialized.
     *
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main FXML file that contains the GUI layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
            Parent root = loader.load();

            // Create a scene with the loaded FXML content
            Scene scene = new Scene(root, 1000, 700);

            // Set up the primary stage (window)
            primaryStage.setTitle("Library Management System");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);  // Minimum window width
            primaryStage.setMinHeight(600); // Minimum window height

            // Show the window
            primaryStage.show();

        } catch (Exception e) {
            // If something goes wrong, print the error
            System.out.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * The main method - entry point of the application.
     * This method launches the JavaFX application.
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
