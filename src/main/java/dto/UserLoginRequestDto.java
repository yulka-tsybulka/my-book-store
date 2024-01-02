package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginRequestDto {
    @NotBlank
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank
    @Size(min = 4, max = 10, message = "Password must be at least 4 characters long")
    private String password;
}
