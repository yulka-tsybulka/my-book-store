package mate.academy.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.dto.book.BookDto;
import mate.academy.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.book.BookSpecificationBuilder;
import mate.academy.bookstore.repository.category.CategoryRepository;
import mate.academy.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    private Book book;
    private Category category;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        category = new Category(1L);
        category.setName("test category");
        category.setDescription("test");
        book = new Book(1L);
        book.setTitle("Test title");
        book.setAuthor("Test author");
        book.setIsbn("0123456789");
        book.setPrice(BigDecimal.valueOf(100.00));
        book.setCategories(Set.of(category));
    }

    @AfterEach
    void tearDown() {
        book = null;
        category = null;
    }

    @Test
    @DisplayName("Verify save book when the book exists")
    public void save_ValidCreateBookRequestDto_ShouldReturnValidBookDto() {
        CreateBookRequestDto requestDto = createBookRequestDto();
        BookDto expected = createBookDto();
        List<Category> categories = List.of(category);
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(expected.getCategoryIds())).thenReturn(categories);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.save(requestDto);
        verify(categoryRepository, times(1)).findAllById(expected.getCategoryIds());
        verify(bookMapper, times(1)).toModel(requestDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify findAll books when the books exists")
    public void findAll_ValidPageable_ShouldReturnAllBooksDto() {
        BookDto bookDto = createBookDto();
        Pageable pageable = PageRequest.of(0, 10);
        String email = "test@email.com";
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        List<BookDto> bookDtos = bookService.findAll(email, pageable);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper,times(1)).toDto(book);
        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Verify findById book when the book exists")
    public void findById_ValidId_ShouldReturnValidBookDto() {
        BookDto expected = createBookDto();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.findById(book.getId());
        verify(bookMapper,times(1)).toDto(book);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify Exception in findById() method when the books id not exists")
    public void findById_WithNonExistingBookId_ShouldThrowException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(book.getId()));
        String expected = "Can't find book by id " + book.getId();
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify update the book when the books id is valid")
    public void update_ValidId_ShouldUpdateBook() {
        CreateBookRequestDto bookRequestDto = createBookRequestDto();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(createBookDto());
        bookService.updateById(1L, bookRequestDto);
    }

    @Test
    @DisplayName("Verify the update() method return Exception when the book with non-existent id")
    public void update_WithNonExistingBookId_ShouldThrowException() {
        book.setId(Long.MAX_VALUE);
        CreateBookRequestDto requestDto = createBookRequestDto();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookService.updateById(book.getId(), requestDto));
        String expected = "Book with id " + book.getId() + " not found";
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify delete book when the books id exists")
    public void delete_ValidId_ShouldDeleteBook() {
        bookService.deleteById(book.getId());
        verify(bookRepository, times(1)).deleteById(book.getId());
    }

    @Test
    @DisplayName("Verify find book by category  when the books exists")
    public void findByCategoryId_ValidId_ShouldReturnBookDtoWithoutCategoryIds() {
        List<Book> books = List.of(book);
        BookDtoWithoutCategoryIds expected = createBookDtoWithoutCategoryIds();
        when(bookRepository.findAllByCategoryId(1L)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(expected);
        List<BookDtoWithoutCategoryIds> actual = bookService.findBooksByCategoryId(1L);
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify search books by valid parameters")
    public void search_ValidParameters_ShouldReturnListBookDto() {
        BookSearchParametersDto searchParameters = createSearchParameters();
        Specification<Book> bookSpecification = Specification.where(null);
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        BookDto bookDto = createBookDto();
        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        List<BookDto> actual = bookService.search(searchParameters, pageable);
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(bookDto);
    }

    private BookSearchParametersDto createSearchParameters() {
        return new BookSearchParametersDto(
                new String[]{book.getTitle()},
                new String[]{book.getAuthor()}
        );
    }

    private CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoryIds(Set.of(1L));
    }

    private BookDto createBookDto() {
        return new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoryIds((Set.of(1L)));
    }

    private BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage());
    }
}
