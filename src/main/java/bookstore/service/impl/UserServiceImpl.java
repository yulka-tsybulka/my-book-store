package bookstore.service.impl;

import bookstore.dto.user.UserRegistrationRequestDto;
import bookstore.dto.user.UserResponseDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.exception.RegistrationException;
import bookstore.mapper.UserMapper;
import bookstore.model.User;
import bookstore.repository.user.UserRepository;
import bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can`t register user");
        }
        return mapper.toDto(userRepository.save(toUserModel(requestDto)));
    }

    private User toUserModel(UserRegistrationRequestDto requestDto) {
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setShippingAddress(requestDto.getShippingAddress());
        return user;
    }
}
