package service;

import java.util.List;
import model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
