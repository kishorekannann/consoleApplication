package service;

import data.DataStore;
import model.Book;
import model.Loan;
import model.PaymentMode;
import model.User;
import repository.BookRepository;
import repository.LoanRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BorrowService {
    private final BookRepository bookRepository = new BookRepository();
    private final LoanRepository loanRepository = new LoanRepository();
    private final FineService fineService = new FineService();

    private final Map<Integer, Set<String>> cart = new HashMap<>();

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public void addToCart(int userId, String isbn) {
        cart.computeIfAbsent(userId, x -> new HashSet<>()).add(isbn);
    }

    public void removeFromCart(int userId, String isbn) {
        cart.computeIfAbsent(userId, x -> new HashSet<>()).remove(isbn);
    }

    public Set<String> getCart(int userId) {
        return cart.getOrDefault(userId, new HashSet<>());
    }

    public String checkout(User borrower) {
        Set<String> items = cart.getOrDefault(borrower.getId(), new HashSet<>());
        if (items.isEmpty()) {
            return "Cart empty";
        }
        if (borrower.getSecurityDeposit() < 500) {
            return "Deposit below 500. Cannot borrow.";
        }
        if (borrower.getOutstandingFine() > borrower.getFineLimit()) {
            return "Outstanding fine exceeds limit.";
        }

        List<Loan> active = loanRepository.findActiveByBorrower(borrower.getId());
        if (active.size() + items.size() > 3) {
            return "Max 3 books allowed.";
        }

        for (String isbn : items) {
            if (loanRepository.findActiveByBorrowerAndIsbn(borrower.getId(), isbn) != null) {
                return "Cannot borrow same book twice: " + isbn;
            }
            Book b = bookRepository.findByIsbn(isbn);
            if (b == null || b.getQuantity() <= 0) {
                return "Unavailable: " + isbn;
            }
        }

        for (String isbn : items) {
            Book b = bookRepository.findByIsbn(isbn);
            b.setQuantity(b.getQuantity() - 1);
            b.incrementBorrowCount();
            loanRepository.save(new Loan(++DataStore.loanSeq, borrower.getId(), isbn, LocalDate.now()));
        }
        items.clear();
        return "Checkout successful.";
    }

    public String extend(User borrower, String isbn) {
        Loan loan = loanRepository.findActiveByBorrowerAndIsbn(borrower.getId(), isbn);
        if (loan == null) {
            return "No active loan found.";
        }
        if (!loan.canExtend()) {
            return "Only two consecutive extensions allowed.";
        }
        loan.extendBy15Days();
        return "Extended to: " + loan.getDueDate();
    }

    public String returnBook(User borrower, String isbn, LocalDate returnDate, PaymentMode mode) {
        Loan loan = loanRepository.findActiveByBorrowerAndIsbn(borrower.getId(), isbn);
        if (loan == null) {
            return "No active loan.";
        }
        Book book = bookRepository.findByIsbn(isbn);

        double fine = fineService.calculateLateFine(book, loan.getBorrowDate(), returnDate);
        if (fine > 0) {
            fineService.applyFine(borrower, fine, "Late return: " + isbn, mode);
        }

        loan.markReturned(returnDate);
        book.setQuantity(book.getQuantity() + 1);
        return "Returned. Fine: " + fine;
    }

    public String markLost(User borrower, String isbn, PaymentMode mode) {
        Loan loan = loanRepository.findActiveByBorrowerAndIsbn(borrower.getId(), isbn);
        if (loan == null) {
            return "No active loan.";
        }
        Book book = bookRepository.findByIsbn(isbn);

        double fine = fineService.lostBookFine(book);
        fineService.applyFine(borrower, fine, "Lost book: " + isbn, mode);
        loan.markLost(LocalDate.now());
        return "Book marked lost. Fine: " + fine;
    }

    public String reportCardLost(User borrower, PaymentMode mode) {
        double fine = fineService.cardLostFine();
        fineService.applyFine(borrower, fine, "Membership card lost", mode);
        return "Card lost fine applied: " + fine;
    }
}
