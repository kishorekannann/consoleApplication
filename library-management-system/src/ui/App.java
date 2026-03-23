package ui;

import model.Book;
import model.PaymentMode;
import model.Role;
import model.User;
import service.AuthService;
import service.BorrowService;
import service.InventoryService;
import service.ReportService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final AuthService authService = new AuthService();
    private static final InventoryService inventoryService = new InventoryService();
    private static final BorrowService borrowService = new BorrowService();
    private static final ReportService reportService = new ReportService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.print("Email: ");
            if (!sc.hasNextLine()) {
                break;
            }
            String email = sc.nextLine();

            System.out.print("Password: ");
            if (!sc.hasNextLine()) {
                break;
            }
            String pass = sc.nextLine();

            User user = authService.login(email, pass);
            if (user == null) {
                System.out.println("Invalid credentials.");
                continue;
            }
            if (user.getRole() == Role.ADMIN) {
                adminMenu();
            } else {
                borrowerMenu(user);
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1.Add Book 2.Modify Qty 3.Delete Book 4.ListByName 5.ListByQty 6.Search");
            System.out.println("7.Add User 8.Set Borrower FineLimit 9.Reports 0.Logout");
            int c = Integer.parseInt(sc.nextLine());
            if (c == 0) {
                break;
            }
            switch (c) {
                case 1:
                    System.out.print("ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Author: ");
                    String author = sc.nextLine();
                    System.out.print("Cost: ");
                    double cost = Double.parseDouble(sc.nextLine());
                    System.out.print("Qty: ");
                    int qty = Integer.parseInt(sc.nextLine());
                    inventoryService.addBook(new Book(isbn, name, author, cost, qty));
                    break;
                case 2:
                    System.out.print("ISBN: ");
                    isbn = sc.nextLine();
                    Book b = inventoryService.searchByIsbn(isbn);
                    if (b != null) {
                        System.out.print("New Qty: ");
                        b.setQuantity(Integer.parseInt(sc.nextLine()));
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 3:
                    System.out.print("ISBN: ");
                    inventoryService.deleteBook(sc.nextLine());
                    break;
                case 4:
                    inventoryService.listByName().forEach(System.out::println);
                    break;
                case 5:
                    inventoryService.listByQuantity().forEach(System.out::println);
                    break;
                case 6:
                    System.out.print("Search by 1.Name 2.ISBN: ");
                    int s = Integer.parseInt(sc.nextLine());
                    if (s == 1) {
                        System.out.print("Name: ");
                        System.out.println(inventoryService.searchByName(sc.nextLine()));
                    } else {
                        System.out.print("ISBN: ");
                        System.out.println(inventoryService.searchByIsbn(sc.nextLine()));
                    }
                    break;
                case 7:
                    System.out.print("Name: ");
                    String uname = sc.nextLine();
                    System.out.print("Email: ");
                    String uemail = sc.nextLine();
                    System.out.print("Password: ");
                    String upass = sc.nextLine();
                    System.out.print("Role (ADMIN/BORROWER): ");
                    Role role = Role.valueOf(sc.nextLine().toUpperCase());
                    System.out.println(inventoryService.addUser(uname, uemail, upass, role));
                    break;
                case 8:
                    System.out.print("Borrower Id: ");
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.print("Fine Limit: ");
                    double lim = Double.parseDouble(sc.nextLine());
                    inventoryService.setBorrowerFineLimit(id, lim);
                    break;
                case 9:
                    adminReports();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void adminReports() {
        System.out.println("1.Low Qty 2.Never Borrowed 3.Heavily Borrowed 4.Outstanding AsOn Date 5.Status By ISBN");
        int r = Integer.parseInt(sc.nextLine());
        switch (r) {
            case 1:
                System.out.print("Threshold: ");
                reportService.lowQuantityBooks(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
            case 2:
                reportService.neverBorrowedBooks().forEach(System.out::println);
                break;
            case 3:
                System.out.print("Min borrow count: ");
                reportService.heavilyBorrowedBooks(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
            case 4:
                System.out.print("Date (dd/MM/yyyy): ");
                reportService.outstandingAsOn(LocalDate.parse(sc.nextLine(), DF)).forEach(System.out::println);
                break;
            case 5:
                System.out.print("ISBN: ");
                System.out.println(reportService.statusByIsbn(sc.nextLine()));
                break;
            default:
                System.out.println("Invalid report option.");
        }
    }

    private static void borrowerMenu(User borrower) {
        while (true) {
            System.out.println("\n--- Borrower Menu ---");
            System.out.println("1.View Books 2.AddToCart 3.RemoveFromCart 4.Checkout 5.Return");
            System.out.println("6.Extend 7.Exchange 8.MarkLostBook 9.ReportCardLost 10.MyFines 11.MyBorrowHistory 0.Logout");
            int c = Integer.parseInt(sc.nextLine());
            if (c == 0) {
                break;
            }
            switch (c) {
                case 1:
                    borrowService.listAllBooks().forEach(System.out::println);
                    break;
                case 2:
                    System.out.print("ISBN: ");
                    borrowService.addToCart(borrower.getId(), sc.nextLine());
                    break;
                case 3:
                    System.out.print("ISBN: ");
                    borrowService.removeFromCart(borrower.getId(), sc.nextLine());
                    break;
                case 4:
                    System.out.println(borrowService.checkout(borrower));
                    break;
                case 5:
                    System.out.print("ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Return Date (dd/MM/yyyy): ");
                    LocalDate rd = LocalDate.parse(sc.nextLine(), DF);
                    System.out.print("Pay fine via CASH/DEPOSIT: ");
                    PaymentMode pm = PaymentMode.valueOf(sc.nextLine().toUpperCase());
                    System.out.println(borrowService.returnBook(borrower, isbn, rd, pm));
                    break;
                case 6:
                    System.out.print("ISBN: ");
                    System.out.println(borrowService.extend(borrower, sc.nextLine()));
                    break;
                case 7:
                    System.out.print("Old ISBN: ");
                    String oldIsbn = sc.nextLine();
                    System.out.print("New ISBN: ");
                    String newIsbn = sc.nextLine();
                    System.out.print("Pay fine via CASH/DEPOSIT: ");
                    pm = PaymentMode.valueOf(sc.nextLine().toUpperCase());
                    System.out.println(borrowService.returnBook(borrower, oldIsbn, LocalDate.now(), pm));
                    borrowService.addToCart(borrower.getId(), newIsbn);
                    System.out.println(borrowService.checkout(borrower));
                    break;
                case 8:
                    System.out.print("ISBN: ");
                    isbn = sc.nextLine();
                    System.out.print("Pay fine via CASH/DEPOSIT: ");
                    pm = PaymentMode.valueOf(sc.nextLine().toUpperCase());
                    System.out.println(borrowService.markLost(borrower, isbn, pm));
                    break;
                case 9:
                    System.out.print("Pay fine via CASH/DEPOSIT: ");
                    pm = PaymentMode.valueOf(sc.nextLine().toUpperCase());
                    System.out.println(borrowService.reportCardLost(borrower, pm));
                    break;
                case 10:
                    reportService.borrowerFines(borrower.getId()).forEach(System.out::println);
                    break;
                case 11:
                    reportService.borrowerHistory(borrower.getId()).forEach(System.out::println);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
            System.out.println("Deposit: " + borrower.getSecurityDeposit() + " | Outstanding: " + borrower.getOutstandingFine());
        }
    }
}
