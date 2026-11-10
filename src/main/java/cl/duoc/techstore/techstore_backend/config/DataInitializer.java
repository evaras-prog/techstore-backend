package cl.duoc.techstore.techstore_backend.config;

import cl.duoc.techstore.techstore_backend.model.Rol;
import cl.duoc.techstore.techstore_backend.model.Usuario;
import cl.duoc.techstore.techstore_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        crearSiNoExiste("Administrador", "admin@techstore.cl", "admin123", Rol.ADMIN);
        crearSiNoExiste("Vendedor", "vendedor@techstore.cl", "vendedor123", Rol.VENDEDOR);
    }

    private void crearSiNoExiste(String nombre, String email, String password, Rol rol) {
        if (!usuarioRepository.existsByEmail(email)) {
            usuarioRepository.save(
                    new Usuario(null, nombre, email, passwordEncoder.encode(password), rol));
        }
    }
}