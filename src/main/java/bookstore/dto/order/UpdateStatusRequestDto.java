package bookstore.dto.order;

import bookstore.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequestDto {
    @NotNull(message = "Can't be empty")
    private Status status;
}
