package bookstore.repository.book;

import bookstore.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b JOIN Fetch b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(@PathVariable(name = "id") Long categoryId);
}
