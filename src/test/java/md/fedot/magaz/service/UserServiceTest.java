package md.fedot.magaz.service;

import md.fedot.magaz.domain.User;
import md.fedot.magaz.model.UserRequestDTO;
import md.fedot.magaz.model.UserResponseDTO;
import md.fedot.magaz.repos.UserRepository;
import md.fedot.magaz.util.DuplicateRecordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("username", "password");

        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        UserResponseDTO result = userService.create(userRequestDTO);

        assertNotNull(result);

        verify(userRepository, times(1)).existsByUsername("username");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUserWithAlreadyExist() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("username", "password");

        when(userRepository.existsByUsername("username")).thenReturn(true);

        assertThrows(DuplicateRecordException.class, () -> userService.create(userRequestDTO));
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1L, "username1", "password1"),
                new User(2L, "username2", "password2")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> result = userService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("username1", result.get(0).getUsername());
    }

    @Test
    public void testGetUsersById() {
        Optional<User> user = Optional.of(new User(1L, "username", "password"));

        when(userRepository.findById(any(Long.class))).thenReturn(user);

        UserResponseDTO result = userService.get(1L);

        assertNotNull(result);

        assertEquals(1L, result.getId());
        assertEquals("username", result.getUsername());

    }

    @Test
    public void testDeleteUserById() {
        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testFindByUsername() {
        Optional<User> mockUser = Optional.of(new User(1L, "testUsername", "testPassword"));

        when(userRepository.findByUsername("testUsername")).thenReturn(mockUser);

        Optional<User> result = userService.findByUsername("testUsername");

        verify(userRepository).findByUsername("testUsername");
    }

}