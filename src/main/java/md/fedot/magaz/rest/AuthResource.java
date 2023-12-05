package md.fedot.magaz.rest;

//import codes.rytis.logindemo.model.LoginRequest;
//import codes.rytis.logindemo.model.LoginResponse;
//import codes.rytis.logindemo.service.AuthService;
import lombok.RequiredArgsConstructor;
import md.fedot.magaz.model.LoginRequest;
import md.fedot.magaz.model.LoginResponse;
import md.fedot.magaz.service.AuthService;
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

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        return authService.attemptLogin(request.getUsername(), request.getPassword());
    }
}
