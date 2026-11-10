package cl.duoc.techstore.techstore_backend.service;

import cl.duoc.techstore.techstore_backend.exception.RecursoNoEncontradoException;
import cl.duoc.techstore.techstore_backend.model.Categoria;
import cl.duoc.techstore.techstore_backend.model.Producto;
import cl.duoc.techstore.techstore_backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("Producto no encontrado: " + id));
    }

    public Producto crear(Producto producto) {
        producto.setId(null);
        producto.setCategoria(resolverCategoria(producto));
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        existente.setCategoria(resolverCategoria(datos));
        return productoRepository.save(existente);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        productoRepository.deleteById(id);
    }

    private Categoria resolverCategoria(Producto producto) {
        return categoriaService.buscarPorId(producto.getCategoria().getId());
    }
}
