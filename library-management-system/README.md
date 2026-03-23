# Library Management System

A console-based library management system built in Java that allows administrators to manage books and users, and enables borrowers to borrow books, pay fines, and view their loan history.

## Features

- **User Authentication**: Role-based login system for Admins and Borrowers
- **Book Management**: Add, modify, delete, and search books by ISBN, name, or author
- **Borrowing System**: Borrow and return books with automatic fine tracking
- **Fine Management**: Calculate and manage overdue fines with payment options
- **User Management**: Add new borrowers, set fine limits, and manage security deposits
- **Reporting**: View detailed reports on books, loans, and fines
- **In-Memory Data Store**: Fast data access using HashMap and ArrayList collections

## Login Credentials

### Admin Account
- **Email**: `admin@lib.com`
- **Password**: `admin123`

### Demo Borrower Account
- **Email**: `s1@lib.com`
- **Password**: `pass123`

## Project Structure

```
library-management-system/
├── src/
│   ├── model/                    # Data models
│   │   ├── Book.java            # Book entity with ISBN, name, author, cost, quantity
│   │   ├── User.java            # User entity with roles, security deposit, fine tracking
│   │   ├── Loan.java            # Loan record with borrow/return dates
│   │   ├── FineRecord.java       # Fine record tracking overdue charges
│   │   ├── PaymentMode.java      # Payment modes (CASH, CARD, CHEQUE, NETBANKING)
│   │   └── Role.java            # User roles (ADMIN, BORROWER)
│   │
│   ├── data/                     # Data storage layer
│   │   └── DataStore.java       # Central in-memory data store with initial data
│   │
│   ├── repository/              # Data access objects
│   │   ├── BookRepository.java  # Book CRUD operations
│   │   ├── UserRepository.java  # User CRUD operations
│   │   ├── LoanRepository.java  # Loan tracking operations
│   │   └── FineRepository.java  # Fine management operations
│   │
│   ├── service/                 # Business logic layer
│   │   ├── AuthService.java     # User authentication
│   │   ├── InventoryService.java # Book inventory management
│   │   ├── BorrowService.java   # Borrowing logic and fine calculation
│   │   ├── FineService.java     # Fine payment processing
│   │   └── ReportService.java   # Report generation
│   │
│   └── ui/                      # User interface
│       └── App.java             # Console interface with admin and borrower menus
│
├── out/                         # Compiled bytecode (auto-generated)
└── README.md                    # This file
```

## Build Instructions

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Terminal/Command prompt access

### Step 1: Navigate to Project Directory
```bash
cd /home/kishorekannann/projects/consoleApplication/library-management-system
```

### Step 2: Compile the Project
```bash
javac -d out src/model/*.java src/data/*.java src/repository/*.java src/service/*.java src/ui/*.java
```

### Step 3: Run the Application
```bash
java -cp out ui.App
```

## Running the Application

1. Start the application using the run command above
2. You'll be prompted for email and password
3. Enter one of the test credentials below
4. Select your desired action from the menu

### Initial Sample Data

**Books:**
- ISBN001 | Java Basics | Author A | Cost: ₹400 | Qty: 5
- ISBN002 | DSA | Author B | Cost: ₹550 | Qty: 3
- ISBN003 | DBMS | Author C | Cost: ₹500 | Qty: 2

**Users:**
- Admin User (id: 1) with full access
- Demo Borrower (id: 2) with borrowing capabilities

## Admin Features

### Book Management
- **Add Book**: Add new books with ISBN, name, author, cost, and quantity
- **Modify Quantity**: Update stock quantity of existing books
- **Delete Book**: Remove books from the system
- **List by Name**: View all books sorted alphabetically
- **List by Quantity**: View books sorted by available quantity
- **Search Books**: Search by name or ISBN

### User Management
- **Add User**: Register new borrowers in the system
- **Set Fine Limit**: Configure maximum fine limit for borrowers (default: ₹1000)

### Reports
- **View Reports**: Access detailed statistics on books, loans, and fines

## Borrower Features

### Borrowing Operations
- **Borrow Books**: Search and borrow available books (up to 5 books, 14-day limit)
- **Return Books**: Return borrowed books with automatic fine calculation
- **View Active Loans**: See all currently borrowed books with due dates

### Fine Management
- **Check Outstanding Fines**: View accumulated overdue charges
- **Pay Fines**: Pay fines using various payment modes:
  - Cash (Direct payment)
  - Card (Credit/Debit)
  - Cheque
  - Net Banking

### View History
- **View Loan History**: Complete record of all past borrowing transactions
- **View Fine History**: Detailed history of paid and outstanding fines

## Business Rules

- **Borrowing Limit**: Maximum 5 books per borrower
- **Return Period**: 14 days per borrowed book
- **Fine Calculation**: ₹2 per day after due date
- **Fine Limit**: Default ₹1000 (configurable by admin)
- **Security Deposit**: Default ₹1500 per borrower
- **Fine Blocking**: Cannot borrow if outstanding fine exceeds fine limit

## Data Persistence

The application uses an in-memory data store (HashMap and ArrayList) for fast access. All data is initialized at startup with default values and is not persisted to disk (data resets on application restart).

## System Architecture

### Layered Architecture
```
UI Layer (App.java)
    ↓
Service Layer (AuthService, InventoryService, BorrowService, etc.)
    ↓
Repository Layer (BookRepository, UserRepository, LoanRepository, etc.)
    ↓
Data Layer (DataStore.java)
```

## Key Classes Overview

| Class | Purpose |
|-------|---------|
| `Book` | Represents a book with ISBN, name, author, cost, and quantity tracking |
| `User` | User profile with role, deposit, fine tracking, and fine limits |
| `Loan` | Records book borrowing with dates and status |
| `FineRecord` | Tracks overdue charges and payments |
| `AuthService` | Handles user login and authentication |
| `InventoryService` | Manages book catalog operations |
| `BorrowService` | Implements borrowing logic and fine calculations |
| `FineService` | Processes fine payments |
| `ReportService` | Generates system reports |

## Future Enhancements

- Database persistence (SQL/NoSQL)
- File I/O for data backup
- ISBN barcode scanning
- Email notifications for due dates
- Advanced search filters
- Book reservations
- Multi-threaded concurrent operations
- Web-based GUI using Spring Boot
- RESTful API for integration

## Troubleshooting

### Compilation Errors
- Ensure all Java files are in the correct package directories
- Verify JDK version compatibility
- Check that the `out` directory exists or create it manually

### Runtime Errors
- Verify login credentials match exactly (case-sensitive)
- Ensure you have the correct role for the action (Admin only features require admin login)
- Check that the input format matches requirements (e.g., ISBN must be a string)

## Contact & Support

For questions or issues, please contact the library management team.

---

**Version**: 1.0  
**Last Updated**: March 2026  
**Status**: Active
