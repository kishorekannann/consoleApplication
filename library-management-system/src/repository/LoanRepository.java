package repository;

import data.DataStore;
import model.Loan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoanRepository {
    public void save(Loan loan) {
        DataStore.loans.add(loan);
    }

    public List<Loan> findAll() {
        return new ArrayList<>(DataStore.loans);
    }

    public List<Loan> findActiveByBorrower(int borrowerId) {
        return DataStore.loans.stream()
                .filter(l -> l.getBorrowerId() == borrowerId && !l.isReturned())
                .collect(Collectors.toList());
    }

    public List<Loan> findByBorrower(int borrowerId) {
        return DataStore.loans.stream()
                .filter(l -> l.getBorrowerId() == borrowerId)
                .collect(Collectors.toList());
    }

    public Loan findActiveByBorrowerAndIsbn(int borrowerId, String isbn) {
        for (Loan l : DataStore.loans) {
            if (l.getBorrowerId() == borrowerId && l.getIsbn().equalsIgnoreCase(isbn) && !l.isReturned()) {
                return l;
            }
        }
        return null;
    }

    public List<Loan> outstandingAsOf(LocalDate date) {
        return DataStore.loans.stream()
                .filter(l -> !l.isReturned() || (l.getReturnDate() != null && l.getReturnDate().isAfter(date)))
                .collect(Collectors.toList());
    }
}
