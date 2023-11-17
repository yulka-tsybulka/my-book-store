package service.impl;

import dto.BookDto;
import dto.BookSearchParameters;
import dto.CreateBookRequestDto;
import exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mapper.BookMapper;
import model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import repository.book.BookRepository;
import repository.book.BookSpecificationBuilder;
import service.BookService;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto).toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by id " + id)
                );
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters parameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
