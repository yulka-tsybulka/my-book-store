package bookstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;
import lombok.NonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequestDto {
    @NonNull
    private String title;

    @NonNull
    private String author;

    @NonNull
    private String isbn;

    @NonNull
    @Min(0)
    private BigDecimal price;

    private String description;

    private String coverImage;
}
