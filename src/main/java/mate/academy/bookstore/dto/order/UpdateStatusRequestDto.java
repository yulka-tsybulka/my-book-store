package mate.academy.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.bookstore.model.Order;

@Data
public class UpdateStatusRequestDto {
    @NotNull(message = "Can't be empty")
    private Order.Status status;
}
