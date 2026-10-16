package com.library.controller;

import com.library.exception.LibraryException;
import com.library.model.*;
import com.library.repository.BookRepository;
import com.library.repository.LoanRepository;
import com.library.repository.MemberRepository;
import com.library.service.BookService;
import com.library.service.LoanService;
import com.library.service.MemberService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//MainController Class
//This is the CONTROLLER in the MVC (Model-View-Controller) pattern. It handles all user interactions from the GUI and communicates with the service layer to perform operations.

//The @FXML annotation is used to link GUI elements defined in the FXML file to variables in this class.


public class MainController implements Initializable {

    // SERVICE LAYER
    // These services handle the business logic

    private BookService bookService;
    private MemberService memberService;
    private LoanService loanService;

    // FILE PATHS FOR DATA STORAGE

    private static final String DATA_FOLDER = "data/";
    private static final String BOOKS_FILE = DATA_FOLDER + "books.txt";
    private static final String MEMBERS_FILE = DATA_FOLDER + "members.txt";
    private static final String LOANS_FILE = DATA_FOLDER + "loans.txt";

    // BOOK TAB COMPONENTS 
    // These are linked to elements in the FXML file

    @FXML private TextField bookTitleField;
    @FXML private TextField bookAuthorField;
    @FXML private TextField bookIsbnField;
    @FXML private TextField bookGenreField;
    @FXML private TextField bookYearField;
    @FXML private TextField bookSearchField;
    @FXML private TableView<LibraryItem> bookTable;
    @FXML private TableColumn<LibraryItem, String> bookIdColumn;
    @FXML private TableColumn<LibraryItem, String> bookTitleColumn;
    @FXML private TableColumn<LibraryItem, String> bookAuthorColumn;
    @FXML private TableColumn<LibraryItem, String> bookGenreColumn;
    @FXML private TableColumn<LibraryItem, Boolean> bookAvailableColumn;

    // MEMBER TAB COMPONENTS 

    @FXML private TextField memberNameField;
    @FXML private TextField memberEmailField;
    @FXML private TextField memberPhoneField;
    @FXML private TextField memberSearchField;
    @FXML private ComboBox<String> memberTypeCombo;

    // Student-specific fields
    @FXML private TextField studentIdField;
    @FXML private TextField studentCourseField;
    @FXML private TextField studentYearField;

    // Staff-specific fields
    @FXML private TextField staffEmployeeIdField;
    @FXML private TextField staffDepartmentField;
    @FXML private TextField staffPositionField;

    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, String> memberIdColumn;
    @FXML private TableColumn<Member, String> memberNameColumn;
    @FXML private TableColumn<Member, String> memberTypeColumn;
    @FXML private TableColumn<Member, String> memberEmailColumn;
    @FXML private TableColumn<Member, Integer> memberBooksColumn;

    // LOAN TAB COMPONENTS

    @FXML private ComboBox<String> loanBookCombo;
    @FXML private ComboBox<String> loanMemberCombo;
    @FXML private TextField returnBookIdField;
    @FXML private TableView<Loan> loanTable;
    @FXML private TableColumn<Loan, String> loanIdColumn;
    @FXML private TableColumn<Loan, String> loanBookColumn;
    @FXML private TableColumn<Loan, String> loanMemberColumn;
    @FXML private TableColumn<Loan, String> loanBorrowDateColumn;
    @FXML private TableColumn<Loan, String> loanDueDateColumn;
    @FXML private TableColumn<Loan, String> loanStatusColumn;

    // REPORT TAB COMPONENTS

    @FXML private Label totalBooksLabel;
    @FXML private Label availableBooksLabel;
    @FXML private Label totalMembersLabel;
    @FXML private Label activeLoansLabel;
    @FXML private Label overdueLoansLabel;
    @FXML private Label totalFinesLabel;
    @FXML private TableView<Loan> overdueTable;
    @FXML private TableColumn<Loan, String> overdueBookColumn;
    @FXML private TableColumn<Loan, String> overdueMemberColumn;
    @FXML private TableColumn<Loan, String> overdueDaysColumn;
    @FXML private TableColumn<Loan, String> overdueFineColumn;

    //  OBSERVABLE LISTS FOR TABLES 
    // These lists automatically update the tables when data changes

    private ObservableList<LibraryItem> bookList;
    private ObservableList<Member> memberList;
    private ObservableList<Loan> loanList;
    private ObservableList<Loan> overdueList;

    // INITIALIZATION 

    // This method is called automatically when the FXML file is loaded. It sets up all the services, loads data from files, and configures the tables.
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Create the repositories (data access layer)
        BookRepository bookRepository = new BookRepository(BOOKS_FILE);
        MemberRepository memberRepository = new MemberRepository(MEMBERS_FILE);
        LoanRepository loanRepository = new LoanRepository(LOANS_FILE);

        // Create the services (business logic layer)
        bookService = new BookService(bookRepository);
        memberService = new MemberService(memberRepository);
        loanService = new LoanService(loanRepository, bookRepository, memberRepository);

        // Create the data folder if it doesn't exist
        createDataFolder();

        // Load existing data from files
        bookService.loadBooks();
        memberService.loadMembers();
        loanService.loadLoans();

        // Initialize the observable lists
        bookList = FXCollections.observableArrayList();
        memberList = FXCollections.observableArrayList();
        loanList = FXCollections.observableArrayList();
        overdueList = FXCollections.observableArrayList();

        // Set up the tables
        setupBookTable();
        setupMemberTable();
        setupLoanTable();
        setupOverdueTable();

        // Set up the member type combo box
        setupMemberTypeCombo();

        // Load initial data into tables
        refreshAllTables();
        refreshComboBoxes();
        updateReportLabels();
    }

    
    // Creates the data folder if it doesn't exist.
    
    private void createDataFolder() {
        java.io.File folder = new java.io.File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    // TABLE SETUP METHODS 

    //Sets up the book table columns.
    
    private void setupBookTable() {
        // Link each column to a property of the LibraryItem/Book class
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // For author, we need special handling since LibraryItem doesn't have author
        bookAuthorColumn.setCellValueFactory(cellData -> {
            LibraryItem item = cellData.getValue();
            if (item instanceof Book) {
                return new javafx.beans.property.SimpleStringProperty(((Book) item).getAuthor());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        // For genre
        bookGenreColumn.setCellValueFactory(cellData -> {
            LibraryItem item = cellData.getValue();
            if (item instanceof Book) {
                return new javafx.beans.property.SimpleStringProperty(((Book) item).getGenre());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        // For availability - show "Available" or "Borrowed"
        bookAvailableColumn.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().isAvailable());
        });

        // Custom cell factory to show "Available" or "Borrowed" text
        bookAvailableColumn.setCellFactory(column -> {
            return new TableCell<LibraryItem, Boolean>() {
                @Override
                protected void updateItem(Boolean available, boolean empty) {
                    super.updateItem(available, empty);
                    if (empty || available == null) {
                        setText("");
                    } else {
                        setText(available ? "Available" : "Borrowed");
                    }
                }
            };
        });

        bookTable.setItems(bookList);
    }

    
    // Sets up the member table columns.
    
    private void setupMemberTable() {
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        memberNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        memberTypeColumn.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMemberType());
        });
        memberEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        memberBooksColumn.setCellValueFactory(new PropertyValueFactory<>("currentBooksCount"));

        memberTable.setItems(memberList);
    }

    
    // Sets up the loan table columns.
    
    private void setupLoanTable() {
        loanIdColumn.setCellValueFactory(new PropertyValueFactory<>("loanId"));

        // For book, show the book ID (or title if we can get it)
        loanBookColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        // For member, show the member ID
        loanMemberColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));

        loanBorrowDateColumn.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBorrowDateFormatted());
        });

        loanDueDateColumn.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDueDateFormatted());
        });

        loanStatusColumn.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            String status;
            if (!loan.isActive()) {
                status = "Returned";
            } else if (loan.isOverdue()) {
                status = "OVERDUE";
            } else {
                status = "Active";
            }
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        loanTable.setItems(loanList);
    }


    // Sets up the overdue loans table columns.
    private void setupOverdueTable() {
        overdueBookColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        overdueMemberColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));

        overdueDaysColumn.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(
                    String.valueOf(cellData.getValue().getDaysOverdue()) + " days"
            );
        });

        overdueFineColumn.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(
                    String.format("£%.2f", cellData.getValue().calculateFine())
            );
        });

        overdueTable.setItems(overdueList);
    }

    // Sets up the member type combo box.

    private void setupMemberTypeCombo() {
        memberTypeCombo.setItems(FXCollections.observableArrayList("Student", "Staff"));
        memberTypeCombo.setValue("Student"); // Default selection
    }

    // BOOK OPERATIONS 

    // Handles the Add Book button click. This method is called from the FXML file.
    
    @FXML
    private void handleAddBook() {
        try {
            // Get values from the text fields
            String title = bookTitleField.getText();
            String author = bookAuthorField.getText();
            String isbn = bookIsbnField.getText();
            String genre = bookGenreField.getText();
            int year = Integer.parseInt(bookYearField.getText());

            // Call the service to add the book
            Book newBook = bookService.addBook(title, author, isbn, genre, year);

            // Show success message
            showInfo("Success", "Book added successfully!\nBook ID: " + newBook.getItemId());

            // Clear the form and refresh the table
            clearBookForm();
            refreshBookTable();
            refreshComboBoxes();
            updateReportLabels();

        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter a valid year.");
        } catch (LibraryException e) {
            showError("Error", e.getMessage());
        }
    }

    // Handles the Search Book button click.
    @FXML
    private void handleSearchBook() {
        String searchTerm = bookSearchField.getText();
        if (searchTerm.isEmpty()) {
            refreshBookTable();
            return;
        }

        // Search by title
        List<LibraryItem> results = bookService.searchByTitle(searchTerm);
        bookList.clear();
        bookList.addAll(results);
    }

    // Handles the Delete Book button click.
    @FXML
    private void handleDeleteBook() {
        // Get selected book from table
        LibraryItem selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showError("No Selection", "Please select a book to delete.");
            return;
        }

        // Confirm deletion
        boolean confirmed = showConfirmation("Confirm Delete",
                "Are you sure you want to delete '" + selectedBook.getTitle() + "'?");

        if (confirmed) {
            try {
                bookService.deleteBook(selectedBook.getItemId());
                showInfo("Success", "Book deleted successfully!");
                refreshBookTable();
                refreshComboBoxes();
                updateReportLabels();
            } catch (LibraryException e) {
                showError("Error", e.getMessage());
            }
        }
    }

    // Clears the book form fields.
    @FXML
    private void clearBookForm() {
        bookTitleField.clear();
        bookAuthorField.clear();
        bookIsbnField.clear();
        bookGenreField.clear();
        bookYearField.clear();
        bookSearchField.clear();
    }

    // MEMBER OPERATIONS 

    // Handles the Add Member button click.
    @FXML
    private void handleAddMember() {
        try {
            // Get common values
            String name = memberNameField.getText();
            String email = memberEmailField.getText();
            String phone = memberPhoneField.getText();
            String memberType = memberTypeCombo.getValue();

            if (memberType.equals("Student")) {
                // Get student-specific values
                String studentId = studentIdField.getText();
                String course = studentCourseField.getText();
                int year = Integer.parseInt(studentYearField.getText());

                // Register the student
                StudentMember newStudent = memberService.registerStudent(name, email, phone,
                        studentId, course, year);

                showInfo("Success", "Student registered successfully!\nMember ID: " + newStudent.getMemberId());

            } else {
                // Get staff-specific values
                String employeeId = staffEmployeeIdField.getText();
                String department = staffDepartmentField.getText();
                String position = staffPositionField.getText();

                // Register the staff member
                StaffMember newStaff = memberService.registerStaff(name, email, phone,
                        employeeId, department, position);

                showInfo("Success", "Staff member registered successfully!\nMember ID: " + newStaff.getMemberId());
            }

            // Clear form and refresh
            clearMemberForm();
            refreshMemberTable();
            refreshComboBoxes();
            updateReportLabels();

        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter a valid year of study.");
        } catch (LibraryException e) {
            showError("Error", e.getMessage());
        }
    }

    // Handles the Search Member button click.
    @FXML
    private void handleSearchMember() {
        String searchTerm = memberSearchField.getText();
        if (searchTerm.isEmpty()) {
            refreshMemberTable();
            return;
        }

        List<Member> results = memberService.searchByName(searchTerm);
        memberList.clear();
        memberList.addAll(results);
    }

    // Handles the Delete Member button click.
    @FXML
    private void handleDeleteMember() {
        Member selectedMember = memberTable.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            showError("No Selection", "Please select a member to delete.");
            return;
        }

        boolean confirmed = showConfirmation("Confirm Delete",
                "Are you sure you want to delete member '" + selectedMember.getName() + "'?");

        if (confirmed) {
            try {
                memberService.deleteMember(selectedMember.getMemberId());
                showInfo("Success", "Member deleted successfully!");
                refreshMemberTable();
                refreshComboBoxes();
                updateReportLabels();
            } catch (LibraryException e) {
                showError("Error", e.getMessage());
            }
        }
    }

    // Clears the member form fields.
    @FXML
    private void clearMemberForm() {
        memberNameField.clear();
        memberEmailField.clear();
        memberPhoneField.clear();
        studentIdField.clear();
        studentCourseField.clear();
        studentYearField.clear();
        staffEmployeeIdField.clear();
        staffDepartmentField.clear();
        staffPositionField.clear();
        memberSearchField.clear();
    }

    //  LOAN OPERATIONS

    // Handles the Borrow Book button click.
    @FXML
    private void handleBorrowBook() {
        try {
            // Get selected values from combo boxes
            String bookSelection = loanBookCombo.getValue();
            String memberSelection = loanMemberCombo.getValue();

            if (bookSelection == null || memberSelection == null) {
                showError("Missing Selection", "Please select both a book and a member.");
                return;
            }

            // Extract IDs from the selections (format: "ID - Title/Name")
            String bookId = bookSelection.split(" - ")[0];
            String memberId = memberSelection.split(" - ")[0];

            // Process the loan
            Loan newLoan = loanService.borrowBook(bookId, memberId);

            showInfo("Success", "Book borrowed successfully!\n" +
                    "Loan ID: " + newLoan.getLoanId() + "\n" +
                    "Due Date: " + newLoan.getDueDateFormatted());

            // Refresh everything
            refreshAllTables();
            refreshComboBoxes();
            updateReportLabels();

        } catch (LibraryException e) {
            showError("Error", e.getMessage());
        }
    }

    // Handles the Return Book button click.
    @FXML
    private void handleReturnBook() {
        try {
            String bookId = returnBookIdField.getText().trim();

            if (bookId.isEmpty()) {
                showError("Missing Input", "Please enter the Book ID to return.");
                return;
            }

            // Process the return
            Loan returnedLoan = loanService.returnBook(bookId);

            String message = "Book returned successfully!";
            if (returnedLoan.getDaysOverdue() > 0) {
                message += "\nThis book was " + returnedLoan.getDaysOverdue() + " days overdue.";
                message += "\nFine: £" + String.format("%.2f", returnedLoan.calculateFine());
            }

            showInfo("Success", message);

            // Clear and refresh
            returnBookIdField.clear();
            refreshAllTables();
            refreshComboBoxes();
            updateReportLabels();

        } catch (LibraryException e) {
            showError("Error", e.getMessage());
        }
    }

    // REFRESH METHODS

    // Refreshes all tables with current data.

    private void refreshAllTables() {
        refreshBookTable();
        refreshMemberTable();
        refreshLoanTable();
        refreshOverdueTable();
    }

    //Refreshes the book table.
    
    private void refreshBookTable() {
        bookList.clear();
        bookList.addAll(bookService.getAllBooks());
    }

    // Refreshes the member table.

    private void refreshMemberTable() {
        memberList.clear();
        memberList.addAll(memberService.getAllMembers());
    }

    // Refreshes the loan table.
    private void refreshLoanTable() {
        loanList.clear();
        loanList.addAll(loanService.getActiveLoans());
    }

    // Refreshes the overdue table.
    private void refreshOverdueTable() {
        overdueList.clear();
        overdueList.addAll(loanService.getOverdueLoans());
    }

    // Refreshes the combo boxes for borrowing.
    private void refreshComboBoxes() {
        // Refresh available books combo box
        ObservableList<String> availableBooks = FXCollections.observableArrayList();
        for (LibraryItem item : bookService.getAvailableBooks()) {
            availableBooks.add(item.getItemId() + " - " + item.getTitle());
        }
        loanBookCombo.setItems(availableBooks);

        // Refresh members combo box
        ObservableList<String> members = FXCollections.observableArrayList();
        for (Member member : memberService.getAllMembers()) {
            members.add(member.getMemberId() + " - " + member.getName());
        }
        loanMemberCombo.setItems(members);
    }

    // Updates the report labels with current statistics.
    @FXML
    private void updateReportLabels() {
        totalBooksLabel.setText(String.valueOf(bookService.getBookCount()));
        availableBooksLabel.setText(String.valueOf(bookService.getAvailableBooks().size()));
        totalMembersLabel.setText(String.valueOf(memberService.getMemberCount()));
        activeLoansLabel.setText(String.valueOf(loanService.getActiveLoanCount()));
        overdueLoansLabel.setText(String.valueOf(loanService.getOverdueLoanCount()));
        totalFinesLabel.setText(String.format("£%.2f", loanService.calculateTotalFines()));

        // Also refresh the overdue table
        refreshOverdueTable();
    }

    // HELPER METHODS FOR DIALOGS

    // Shows an information dialog.
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Shows an error dialog.
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Shows a confirmation dialog.
    
    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
