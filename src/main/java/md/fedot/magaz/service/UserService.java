package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fedot.magaz.dto.UserRequestDto;
import md.fedot.magaz.dto.UserResponseDto;
import md.fedot.magaz.exception.DuplicateRecordException;
import md.fedot.magaz.exception.NotFoundException;
import md.fedot.magaz.model.User;
import md.fedot.magaz.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> getAllUsers() {
        log.info("Getting all users...");

        List<UserResponseDto> userResponseDtos = userRepository.findAll()
                .stream()
                .map(UserResponseDto::new)
                .toList();

        log.info("Found {} users", userResponseDtos.size());

        return userResponseDtos;
    }

    public UserResponseDto getUser(Long id) {
        log.info("Getting user with ID: " + id);

        UserResponseDto userResponseDto = userRepository.findById(id)
                .map(UserResponseDto::new)
                .orElseThrow(() -> {
                    log.warn("User not found");
                    return new NotFoundException("User not found");
                });

        log.info("Found user: " + userResponseDto);

        return userResponseDto;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Creating user with data: " + userRequestDto);

        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            log.warn("This username is taken");
            throw new DuplicateRecordException("This username is taken");
        }

        User user = mapToEntity(userRequestDto, new User());

        try {
            User savedUser = userRepository.save(user);
            log.info("User created with ID: " + savedUser.getId());
        } catch (Exception e) {
            log.warn("User was not created: " + e.getMessage());
        }

        return new UserResponseDto(user);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            log.warn("This username is taken");
            throw new DuplicateRecordException("This username is taken");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found");
                    return new NotFoundException("User not found");
                });

        mapToEntity(userRequestDto, user);

        try {
            userRepository.save(user);
            log.info("User updated");
        } catch (Exception e) {
            log.error("User was not updated: " + e.getMessage());
        }

        return new UserResponseDto(user);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: " + id);

        try {
            userRepository.deleteById(id);
            log.info("User deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting user: " + e.getMessage());
        }
    }

    public User mapToEntity(UserRequestDto userRequestDTO, User user) {
        user.setUsername(userRequestDTO.getUsername());
        String hashedPassword  = BCrypt.hashpw(userRequestDTO.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hashedPassword);
        return user;
    }

}
