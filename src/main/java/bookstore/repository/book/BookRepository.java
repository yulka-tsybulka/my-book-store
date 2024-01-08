package bookstore.repository.book;

import bookstore.model.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAll(Specification<Book> specification);

    Page<Book> findAll(String email, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN Fetch b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(@PathVariable(name = "id") Long categoryId);
}
