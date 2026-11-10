package cl.duoc.techstore.techstore_backend.repository;

import cl.duoc.techstore.techstore_backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}