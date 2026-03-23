package repository;

import data.DataStore;
import model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    public List<Book> findAll() {
        return new ArrayList<>(DataStore.books.values());
    }

    public Book findByIsbn(String isbn) {
        return DataStore.books.get(isbn);
    }

    public void save(Book b) {
        DataStore.books.put(b.getIsbn(), b);
    }

    public void delete(String isbn) {
        DataStore.books.remove(isbn);
    }
}
