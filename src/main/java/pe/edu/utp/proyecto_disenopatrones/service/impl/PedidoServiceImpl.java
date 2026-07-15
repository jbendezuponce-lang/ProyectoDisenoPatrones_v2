package pe.edu.utp.proyecto_disenopatrones.service.impl;

import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.util.FechaUtil;
import pe.edu.utp.proyecto_disenopatrones.model.Pedido;
import pe.edu.utp.proyecto_disenopatrones.model.DetallePedido;
import pe.edu.utp.proyecto_disenopatrones.model.Producto;
import pe.edu.utp.proyecto_disenopatrones.repository.PedidoRepository;
import pe.edu.utp.proyecto_disenopatrones.repository.ProductoRepository;
import pe.edu.utp.proyecto_disenopatrones.service.InventarioService;
import pe.edu.utp.proyecto_disenopatrones.service.PagoService;
import pe.edu.utp.proyecto_disenopatrones.service.ComprobanteService;
import pe.edu.utp.proyecto_disenopatrones.service.PedidoService;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.MetodoPago;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Implementación de {@link pe.edu.utp.proyecto_disenopatrones.service.PedidoService}.
 * Es el orquestador principal del negocio: crea pedidos con sus detalles y,
 * en {@link #procesarVenta}, coordina en una sola transacción los 4 patrones
 * de diseño del proyecto:
 * <ol>
 * <li><b>Strategy</b>: recibe la estrategia de pago elegida (Yape, Tarjeta, Plin, Efectivo).</li>
 * <li>Valida y descuenta stock vía {@link InventarioService}.</li>
 * <li>Registra el pago vía {@link PagoService}.</li>
 * <li><b>Factory Method + Decorator + Singleton</b>: genera el comprobante
 * (Boleta/Factura con descuento e IGV) vía {@link ComprobanteService}.</li>
 * </ol>
 */
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final ProductoRepository productoRepository;
    private final InventarioService inventarioService;
    private final PagoService pagoService;
    private final ComprobanteService comprobanteService;

    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_COMPLETADO = "COMPLETADO";

    @Override
    @Transactional
    public Pedido guardar(Pedido pedido) {
        log.info("Guardando pedido inicial");

        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(FechaUtil.ahora());
        }
        pedido.setEstado(ESTADO_PENDIENTE);

        procesarDetalles(pedido, pedido.getDetalles());
        return repository.save(pedido);
    }

    /**
     * Enlaza cada {@link DetallePedido} con su {@link Pedido} (relación
     * bidireccional obligatoria), trae el precio real del {@link Producto}
     * desde la BD (nunca se confía en el precio que manda el cliente) y
     * recalcula subtotal y total. Se reutiliza tanto en {@link #guardar}
     * como en {@link #actualizar}.
     */
    private void procesarDetalles(Pedido pedido, List<DetallePedido> detalles) {
        double totalCalculado = 0.0;

        if (detalles != null) {
            for (DetallePedido detalle : detalles) {
                if (detalle.getProducto() == null || detalle.getProducto().getId() == null) {
                    throw new IllegalArgumentException("Cada detalle debe indicar el ID de un producto existente");
                }
                Producto producto = productoRepository.findById(detalle.getProducto().getId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Producto con ID " + detalle.getProducto().getId() + " no encontrado"));

                if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                    throw new IllegalArgumentException("La cantidad de cada detalle debe ser mayor a 0");
                }

                detalle.setProducto(producto);
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
                detalle.setPedido(pedido); // <- enlace bidireccional obligatorio

                totalCalculado += detalle.getSubtotal();
            }
        }
        pedido.setTotal(totalCalculado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return obtenerPedidoDesdeBD(id);
    }

    /**
     * Método interno privado sin anotaciones de Spring (ni @Transactional ni @Cacheable)
     * para evitar el "Problema de Auto-Invocación de Proxies" detectado por SonarQube.
     */
    private Pedido obtenerPedidoDesdeBD(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado"));
    }

    @Override
    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        // Se llama al método privado para evitar romper el proxy @Transactional
        Pedido pedido = obtenerPedidoDesdeBD(id);
        pedido.setEstado(nuevoEstado);
        return repository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido actualizar(Long id, Pedido pedidoActualizado) {
        log.info("Actualizando pedido ID: {}", id);
        // Se llama al método privado para evitar romper el proxy @Transactional
        Pedido existente = obtenerPedidoDesdeBD(id);

        // Regla de negocio: un pedido ya completado (con pago/comprobante emitido)
        // no debe poder editarse; evita inconsistencias con lo ya facturado.
        if (ESTADO_COMPLETADO.equalsIgnoreCase(existente.getEstado())) {
            throw new IllegalStateException("El pedido " + id + " ya fue completado y no puede modificarse");
        }

        if (pedidoActualizado.getCliente() != null) {
            existente.setCliente(pedidoActualizado.getCliente());
        }

        // Reemplaza por completo los detalles (orphanRemoval=true elimina los anteriores)
        // y recalcula el total con precios reales de la BD.
        if (pedidoActualizado.getDetalles() != null) {
            existente.getDetalles().clear();
            existente.getDetalles().addAll(pedidoActualizado.getDetalles());
            procesarDetalles(existente, existente.getDetalles());
        }

        return repository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando pedido ID: {}", id);
        // Se llama al método privado para evitar romper el proxy @Transactional
        Pedido pedido = obtenerPedidoDesdeBD(id);

        // No se permite borrar un pedido ya completado: ya tiene Pago/Comprobante
        // asociados (FK) y borrarlo rompería la trazabilidad de la venta.
        if (ESTADO_COMPLETADO.equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalStateException("El pedido " + id + " ya fue completado y no puede eliminarse");
        }
        repository.delete(pedido);
    }

    @Override
    @Transactional
    public String procesarVenta(Long pedidoId, String tipoDocumento, MetodoPago metodoPago) {
        log.info("Iniciando procesamiento de venta para Pedido ID: {}", pedidoId);
        // Se llama al método privado para evitar romper el proxy @Transactional
        Pedido pedido = obtenerPedidoDesdeBD(pedidoId);

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalStateException("El pedido " + pedidoId + " no tiene detalles/productos para vender");
        }
        if (ESTADO_COMPLETADO.equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalStateException("El pedido " + pedidoId + " ya fue procesado anteriormente");
        }

        // 1. PATRÓN STRATEGY: valida y descuenta stock por cada producto del pedido
        for (DetallePedido detalle : pedido.getDetalles()) {
            Long idProd = detalle.getProducto().getId();
            Integer cant = detalle.getCantidad();

            inventarioService.validarStock(idProd, cant);
            inventarioService.descontarStock(idProd, cant);
        }

        // 2. PATRÓN STRATEGY: procesa el pago con el método elegido (Yape, Tarjeta, Plin, Efectivo)
        pagoService.registrarPago(pedido, pedido.getTotal(), metodoPago.getNombre());

        // 3. PATRÓN FACTORY + DECORATOR: genera Boleta/Factura con IGV y/o descuento
        comprobanteService.generarComprobante(pedido, tipoDocumento);

        pedido.setEstado(ESTADO_COMPLETADO);
        repository.save(pedido);

        return "Venta procesada exitosamente con comprobante: " + tipoDocumento
                + " | Método de pago: " + metodoPago.getNombre()
                + " | Total: S/ " + pedido.getTotal();
    }
}