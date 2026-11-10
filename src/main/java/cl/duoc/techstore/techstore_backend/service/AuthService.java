package cl.duoc.techstore.techstore_backend.service;

import cl.duoc.techstore.techstore_backend.dto.AuthResponse;
import cl.duoc.techstore.techstore_backend.dto.LoginRequest;
import cl.duoc.techstore.techstore_backend.dto.RegistroRequest;
import cl.duoc.techstore.techstore_backend.exception.ConflictoException;
import cl.duoc.techstore.techstore_backend.exception.CredencialesInvalidasException;
import cl.duoc.techstore.techstore_backend.model.Rol;
import cl.duoc.techstore.techstore_backend.model.Usuario;
import cl.duoc.techstore.techstore_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse registrar(RegistroRequest datos) {
        if (usuarioRepository.existsByEmail(datos.email())) {
            throw new ConflictoException("El email ya está registrado: " + datos.email());
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(datos.nombre());
        usuario.setEmail(datos.email());
        usuario.setPassword(passwordEncoder.encode(datos.password()));
        usuario.setRol(Rol.CLIENTE);
        usuarioRepository.save(usuario);

        return construirRespuesta(usuario);
    }

    public AuthResponse login(LoginRequest datos) {
        Usuario usuario = usuarioRepository.findByEmail(datos.email())
                .orElseThrow(()-> new CredencialesInvalidasException("Credenciales inválidas"));

        if (!passwordEncoder.matches(datos.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }
        return construirRespuesta(usuario);
    }

    private AuthResponse construirRespuesta(Usuario usuario) {
        return new AuthResponse(jwtService.generarToken(usuario),
                usuario.getNombre(), usuario.getEmail(), usuario.getRol().name());
    }
}