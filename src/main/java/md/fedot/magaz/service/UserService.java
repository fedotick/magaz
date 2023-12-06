package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import md.fedot.magaz.dto.UserRequestDto;
import md.fedot.magaz.dto.UserResponseDto;
import md.fedot.magaz.exception.DuplicateRecordException;
import md.fedot.magaz.exception.NotFoundException;
import md.fedot.magaz.model.User;
import md.fedot.magaz.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> mapToDTO(user, new UserResponseDto()))
                .toList();
    }

    public UserResponseDto getUser(Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserResponseDto()))
                .orElseThrow(NotFoundException::new);
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new DuplicateRecordException("This username is taken!");
        }
        User user = mapToEntity(userRequestDto, new User());
        return mapToDTO(userRepository.save(user), new UserResponseDto());
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new DuplicateRecordException("This username is taken!");
        }
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userRequestDto, user);
        return mapToDTO(userRepository.save(user), new UserResponseDto());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User mapToEntity(UserRequestDto userRequestDTO, User user) {
        user.setUsername(userRequestDTO.getUsername());
        String hashedPassword  = BCrypt.hashpw(userRequestDTO.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hashedPassword);
        return user;
    }

    public UserResponseDto mapToDTO(User user, UserResponseDto userResponseDto) {
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        return userResponseDto;
    }

}
