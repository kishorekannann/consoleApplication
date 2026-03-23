package data;

import model.Book;
import model.Role;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    public static final Map<String, Book> books = new HashMap<>();
    public static final Map<Integer, User> users = new HashMap<>();
    public static final List<model.Loan> loans = new ArrayList<>();
    public static final List<model.FineRecord> fines = new ArrayList<>();

    public static int userSeq = 2;
    public static int loanSeq = 0;
    public static int fineSeq = 0;

    static {
        users.put(1, new User(1, "Admin", "admin@lib.com", "admin123", Role.ADMIN));
        users.put(2, new User(2, "Student1", "s1@lib.com", "pass123", Role.BORROWER));

        books.put("ISBN001", new Book("ISBN001", "Java Basics", "Author A", 400, 5));
        books.put("ISBN002", new Book("ISBN002", "DSA", "Author B", 550, 3));
        books.put("ISBN003", new Book("ISBN003", "DBMS", "Author C", 500, 2));
    }
}
