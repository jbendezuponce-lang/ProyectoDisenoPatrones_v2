package pe.edu.utp.proyecto_disenopatrones.service.impl;

import pe.edu.utp.proyecto_disenopatrones.exception.NegocioException;
import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Producto;
import pe.edu.utp.proyecto_disenopatrones.repository.ProductoRepository;
import pe.edu.utp.proyecto_disenopatrones.service.InventarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Implementación de {@link pe.edu.utp.proyecto_disenopatrones.service.InventarioService}.
 * Controla el stock de productos: valida disponibilidad antes de vender,
 * descuenta unidades al confirmar una venta y permite reponer stock.
 * Es invocado por {@link PedidoServiceImpl#procesarVenta} antes de registrar el pago.
 */
public class InventarioServiceImpl implements InventarioService {

    private static final String PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";

    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public void validarStock(Long productoId, Integer cantidadRequerida) {
        log.info("Validando stock para producto ID: {}", productoId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(PRODUCTO_NO_ENCONTRADO));

        if (producto.getStock() < cantidadRequerida) {
            throw new NegocioException("Stock insuficiente para el producto: " + producto.getNombre());
        }
    }

    @Override
    @Transactional
    public void descontarStock(Long productoId, Integer cantidad) {
        log.info("Descontando {} unidades del producto ID: {}", cantidad, productoId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(PRODUCTO_NO_ENCONTRADO));

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void aumentarStock(Long productoId, Integer cantidad) {
        log.info("Aumentando {} unidades al producto ID: {}", cantidad, productoId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(PRODUCTO_NO_ENCONTRADO));

        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer consultarExistencias(Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(PRODUCTO_NO_ENCONTRADO));
        return producto.getStock();
    }
}
