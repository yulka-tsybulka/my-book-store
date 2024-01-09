package bookstore.dto.book;

import bookstore.validation.Isbn;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequestDto {
    @NonNull
    private String title;
    @NonNull
    private String author;
    @Isbn
    @NonNull
    private String isbn;
    @NonNull
    @Min(0)
    private BigDecimal price;
    @Size(max = 255)
    private String description;
    @Size(max = 255)
    private String coverImage;
    private Set<Long> categoryIds = new HashSet<>();
}
