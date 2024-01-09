package bookstore.service;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.book.BookSearchParameters;
import bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(String email, Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters parameters, Pageable pageable);

    List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId);

    BookDto updateById(Long id, CreateBookRequestDto createBookRequestDto);
}
