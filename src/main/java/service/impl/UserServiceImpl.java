package service.impl;

import dto.UserRegistrationRequestDto;
import dto.UserResponseDto;
import exception.EntityNotFoundException;
import exception.RegistrationException;
import lombok.RequiredArgsConstructor;
import mapper.UserMapper;
import model.User;
import org.springframework.stereotype.Service;
import repository.user.UserRepository;
import service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper mapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can`t register user");
        }
        return mapper.toDto(userRepository.save(toUserModel(requestDto)));
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        return mapper.toDto(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "
                        + email)));
    }

    private User toUserModel(UserRegistrationRequestDto requestDto) {
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setPassword(requestDto.getPassword());
        user.setShippingAddress(requestDto.getShippingAddress());
        return user;
    }
}
