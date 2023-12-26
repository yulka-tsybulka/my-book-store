package service;

import dto.UserRegistrationRequestDto;
import dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    UserResponseDto findByEmail(String email);
}
