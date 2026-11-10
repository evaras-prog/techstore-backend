package cl.duoc.techstore.techstore_backend.service;

import cl.duoc.techstore.techstore_backend.exception.RecursoNoEncontradoException;
import cl.duoc.techstore.techstore_backend.model.Categoria;
import cl.duoc.techstore.techstore_backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada: " + id));
    }

    public Categoria crear(Categoria categoria) {
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria datos) {
        Categoria existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        return categoriaRepository.save(existente);
    }

    public void eliminar(Long id) {
        Categoria existente = buscarPorId(id);
        categoriaRepository.delete(existente);
    }
}
