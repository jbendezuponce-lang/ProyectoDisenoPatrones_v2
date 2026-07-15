package pe.edu.utp.proyecto_disenopatrones.service.impl;

import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Producto;
import pe.edu.utp.proyecto_disenopatrones.repository.ProductoRepository;
import pe.edu.utp.proyecto_disenopatrones.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Implementación de {@link pe.edu.utp.proyecto_disenopatrones.service.ProductoService}.
 * CRUD de productos del catálogo (crear, listar, buscar, eliminar) y
 * actualización directa de stock (uso administrativo, distinto del
 * descuento automático que hace {@link InventarioServiceImpl} durante una venta).
 */
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        log.info("Iniciando guardado de producto: {}", producto.getNombre());
        // Sin try/catch que enmascare el error: si la categoria_id no existe, el
        // GlobalExceptionHandler lo traduce a un 400 claro en vez de un 500 genérico.
        return repository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listar() {
        log.info("Listando todos los productos");
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        return obtenerProductoDesdeBD(id);
    }

    /**
     * Método interno sin anotaciones para evitar el problema de auto-invocación de proxies
     * detectado por SonarQube (Call transactional methods via an injected dependency).
     */
    private Producto obtenerProductoDesdeBD(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado"));
    }

    @Override
    @Transactional
    public Producto actualizar(Long id, Producto producto) {
        log.info("Actualizando producto ID: {}", id);
        // Llamada al método privado en lugar de buscarPorId(id)
        Producto existente = obtenerProductoDesdeBD(id);

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setCategoria(producto.getCategoria());

        return repository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando producto con ID: {}", id);
        // Llamada al método privado en lugar de buscarPorId(id)
        Producto producto = obtenerProductoDesdeBD(id);
        repository.delete(producto);
    }

    @Override
    @Transactional
    public void actualizarStock(Long id, Integer cantidad) {
        log.info("Actualizando stock del producto {}. Nueva cantidad: {}", id, cantidad);
        // Llamada al método privado en lugar de buscarPorId(id)
        Producto producto = obtenerProductoDesdeBD(id);
        producto.setStock(cantidad);
        repository.save(producto);
    }
}