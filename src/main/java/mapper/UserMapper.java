package mapper;

import config.MapperConfig;
import dto.UserRegistrationRequestDto;
import dto.UserResponseDto;
import model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
