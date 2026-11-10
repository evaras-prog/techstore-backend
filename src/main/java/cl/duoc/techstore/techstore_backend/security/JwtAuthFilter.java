package cl.duoc.techstore.techstore_backend.security;

import cl.duoc.techstore.techstore_backend.repository.UsuarioRepository;
import cl.duoc.techstore.techstore_backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String cabecera = request.getHeader("Authorization");

        if (cabecera != null && cabecera.startsWith("Bearer ")) {
            String token = cabecera.substring(7);

            if (jwtService.esValido(token)) {
                String email = jwtService.extraerEmail(token);

                usuarioRepository.findByEmail(email).ifPresent(usuario -> {
                    var autoridad = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());
                    var autenticacion = new UsernamePasswordAuthenticationToken(email, null, List.of(autoridad));
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                });
            }
        }

        filterChain.doFilter(request, response);
    }
}