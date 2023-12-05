package md.fedot.magaz.rest;

import lombok.RequiredArgsConstructor;
import md.fedot.magaz.model.LoginResponse;
import md.fedot.magaz.model.UserRequestDTO;
import md.fedot.magaz.model.UserResponseDTO;
import md.fedot.magaz.service.AuthService;
import md.fedot.magaz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthResource {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated UserRequestDTO request) {
        return authService.attemptLogin(request.getUsername(), request.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Validated UserRequestDTO request) {
        return ResponseEntity.ok(userService.create(request));
    }
}
