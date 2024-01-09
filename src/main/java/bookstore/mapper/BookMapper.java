package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.model.Book;
import bookstore.model.Category;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        book.setCategories(requestDto.getCategoryIds()
                .stream()
                .map(Category::new)
                .collect(Collectors.toSet()));
    }

    default Book bookFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Book::new)
                .orElse(null);
    }
}
