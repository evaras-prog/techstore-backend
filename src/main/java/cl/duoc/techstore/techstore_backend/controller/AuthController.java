package cl.duoc.techstore.techstore_backend.controller;

import cl.duoc.techstore.techstore_backend.dto.AuthResponse;
import cl.duoc.techstore.techstore_backend.dto.LoginRequest;
import cl.duoc.techstore.techstore_backend.dto.RegistroRequest;
import cl.duoc.techstore.techstore_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registro(@Valid @RequestBody RegistroRequest datos) {
        return authService.registrar(datos);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest datos) {
        return authService.login(datos);
    }

    @GetMapping("/yo")
    public Map<String, Object> yo(Authentication autenticacion) {
        return Map.of(
                "email", autenticacion.getName(),
                "roles", autenticacion.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());
    }
}
