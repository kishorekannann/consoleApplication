package service;

import data.DataStore;
import model.Book;
import model.Role;
import model.User;
import repository.BookRepository;
import repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {
    private final BookRepository bookRepository = new BookRepository();
    private final UserRepository userRepository = new UserRepository();

    public void addBook(Book b) {
        bookRepository.save(b);
    }

    public void deleteBook(String isbn) {
        bookRepository.delete(isbn);
    }

    public List<Book> listByName() {
        return bookRepository.findAll().stream()
                .sorted(Comparator.comparing(Book::getName))
                .collect(Collectors.toList());
    }

    public List<Book> listByQuantity() {
        return bookRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Book::getQuantity))
                .collect(Collectors.toList());
    }

    public Book searchByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public Book searchByName(String name) {
        for (Book b : bookRepository.findAll()) {
            if (b.getName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }

    public User addUser(String name, String email, String password, Role role) {
        User u = new User(++DataStore.userSeq, name, email, password, role);
        userRepository.save(u);
        return u;
    }

    public void setBorrowerFineLimit(int borrowerId, double limit) {
        User u = userRepository.findById(borrowerId);
        if (u != null && u.getRole() == Role.BORROWER) {
            u.setFineLimit(limit);
        }
    }
}
