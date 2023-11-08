package repository;

import java.util.List;
import model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
