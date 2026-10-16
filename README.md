# Library Management System (Java + JavaFX)

A desktop application for managing a library's books, members and loans, with a JavaFX interface and a layered, object-oriented design.

## What it does

- Add, search and manage **books and e-books**
- Register **student and staff members**, each with their own borrowing limits
- **Lend and return** items, enforcing availability and borrowing limits
- Save all data to files so it persists between sessions

## Design

The code is split into clear layers, which keeps each part easy to test and change:

| Layer | Responsibility |
|---|---|
| `model` | Domain classes using inheritance: `LibraryItem` → `Book`, `EBook`; `Member` → `StudentMember`, `StaffMember` |
| `repository` | Loading and saving books, members and loans through a `Persistable` interface |
| `service` | Business rules, such as checking availability and borrowing limits before a loan |
| `controller` | Connects the JavaFX view (`MainView.fxml`) to the services |
| `exception` | Custom exceptions such as `BookNotAvailableException` and `BorrowingLimitExceededException` |
| `util` | Input validation and ID generation |

## Run it

Requirements: JDK 17 and Maven.

```bash
mvn clean javafx:run
```

Sample data lives in the `data/` folder (fictional books and members).
