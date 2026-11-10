package cl.duoc.techstore.techstore_backend.dto;

public record AuthResponse(String token, String nombre, String email, String rol) {
}
