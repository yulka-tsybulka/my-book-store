package bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddToCartRequestDto {
    private Long bookId;
    @Positive
    private int quantity;
}
