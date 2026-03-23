package service;

import model.Book;
import model.FineRecord;
import model.Loan;
import model.User;
import repository.BookRepository;
import repository.FineRepository;
import repository.LoanRepository;
import repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {
    private final BookRepository bookRepo = new BookRepository();
    private final LoanRepository loanRepo = new LoanRepository();
    private final UserRepository userRepo = new UserRepository();
    private final FineRepository fineRepo = new FineRepository();

    public List<Book> lowQuantityBooks(int threshold) {
        return bookRepo.findAll().stream()
                .filter(b -> b.getQuantity() < threshold)
                .collect(Collectors.toList());
    }

    public List<Book> neverBorrowedBooks() {
        return bookRepo.findAll().stream()
                .filter(b -> b.getBorrowCount() == 0)
                .collect(Collectors.toList());
    }

    public List<Book> heavilyBorrowedBooks(int count) {
        return bookRepo.findAll().stream()
                .filter(b -> b.getBorrowCount() >= count)
                .collect(Collectors.toList());
    }

    public List<Loan> outstandingAsOn(LocalDate date) {
        return loanRepo.outstandingAsOf(date);
    }

    public String statusByIsbn(String isbn) {
        for (Loan l : loanRepo.findAll()) {
            if (l.getIsbn().equalsIgnoreCase(isbn) && !l.isReturned()) {
                User u = userRepo.findById(l.getBorrowerId());
                return "Borrowed by: " + (u != null ? u.getName() : l.getBorrowerId()) + " | Due: " + l.getDueDate();
            }
        }
        return "Book currently in rack or no active loan.";
    }

    public List<FineRecord> borrowerFines(int borrowerId) {
        return fineRepo.findByBorrower(borrowerId);
    }

    public List<Loan> borrowerHistory(int borrowerId) {
        return loanRepo.findByBorrower(borrowerId);
    }
}
