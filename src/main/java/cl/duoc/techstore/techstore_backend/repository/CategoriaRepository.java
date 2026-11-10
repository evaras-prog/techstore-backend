package cl.duoc.techstore.techstore_backend.repository;

import cl.duoc.techstore.techstore_backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

