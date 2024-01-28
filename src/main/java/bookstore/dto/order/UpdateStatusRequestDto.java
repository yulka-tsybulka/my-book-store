package bookstore.dto.order;

import bookstore.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequestDto {
    @NotNull(message = "Can't be empty")
    private Order.Status status;
}
