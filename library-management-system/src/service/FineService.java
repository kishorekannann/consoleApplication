package service;

import data.DataStore;
import model.Book;
import model.FineRecord;
import model.PaymentMode;
import model.User;
import repository.FineRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineService {
    private final FineRepository fineRepository = new FineRepository();

    public double calculateLateFine(Book book, LocalDate borrowDate, LocalDate returnDate) {
        long days = ChronoUnit.DAYS.between(borrowDate, returnDate);
        long overdue = days - 15;
        if (overdue <= 0) {
            return 0.0;
        }

        double base = overdue * 2.0;
        long periods = overdue / 10;
        double fine = base * Math.pow(2, periods);
        return Math.min(fine, book.getCost() * 0.80);
    }

    public double lostBookFine(Book book) {
        return book.getCost() * 0.50;
    }

    public double cardLostFine() {
        return 10.0;
    }

    public void applyFine(User user, double amount, String reason, PaymentMode mode) {
        if (amount <= 0) {
            return;
        }

        if (mode == PaymentMode.DEPOSIT) {
            user.setSecurityDeposit(Math.max(0, user.getSecurityDeposit() - amount));
        } else {
            user.addOutstandingFine(amount);
        }

        FineRecord fine = new FineRecord(++DataStore.fineSeq, user.getId(), amount, reason, LocalDate.now(), mode);
        fineRepository.save(fine);
    }
}
