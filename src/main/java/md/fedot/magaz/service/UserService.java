package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import md.fedot.magaz.domain.User;
import md.fedot.magaz.model.UserRequestDTO;
import md.fedot.magaz.model.UserResponseDTO;
import md.fedot.magaz.repos.UserRepository;
import md.fedot.magaz.util.DuplicateRecordException;
import md.fedot.magaz.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO create(final UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new DuplicateRecordException("This username is taken!");
        }
        final User user = mapToEntity(userRequestDTO, new User());
        return mapToDTO(userRepository.save(user), new UserResponseDTO());
    }

    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream()
                .map(user -> mapToDTO(user, new UserResponseDTO()))
                .toList();
    }

    public UserResponseDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UserResponseDTO update(final Long id, final UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new DuplicateRecordException("This username is taken!");
        }
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userRequestDTO, user);
        return mapToDTO(userRepository.save(user), new UserResponseDTO());
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    public User mapToEntity(final UserRequestDTO userRequestDTO, final User user) {
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(userRequestDTO.getPassword());
        return user;
    }

    public UserResponseDTO mapToDTO(final User user, final UserResponseDTO userResponseDTO) {
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        return userResponseDTO;
    }

}
