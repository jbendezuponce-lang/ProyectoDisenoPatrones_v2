package pe.edu.utp.proyecto_disenopatrones.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.proyecto_disenopatrones.model.*;
import pe.edu.utp.proyecto_disenopatrones.repository.PedidoRepository;
import pe.edu.utp.proyecto_disenopatrones.repository.ProductoRepository;
import pe.edu.utp.proyecto_disenopatrones.service.ComprobanteService;
import pe.edu.utp.proyecto_disenopatrones.service.InventarioService;
import pe.edu.utp.proyecto_disenopatrones.service.PagoService;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.PagoYape;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock private PedidoRepository repository;
    @Mock private ProductoRepository productoRepository;
    @Mock private InventarioService inventarioService;
    @Mock private PagoService pagoService;
    @Mock private ComprobanteService comprobanteService;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void listarDebeRetornarTodosLosPedidos() {
        Pedido pedido = Pedido.builder().id(1L).build();
        when(repository.findAll()).thenReturn(List.of(pedido));

        assertThat(pedidoService.listar()).hasSize(1).containsExactly(pedido);
    }

    @Test
    void buscarPorIdDebeRetornarElPedidoCuandoExiste() {
        Pedido pedido = Pedido.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThat(pedidoService.buscarPorId(1L)).isEqualTo(pedido);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void actualizarEstadoDebeModificarYGuardarElNuevoEstado() {
        Pedido pedido = Pedido.builder().id(1L).estado("PENDIENTE").build();
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = pedidoService.actualizarEstado(1L, "CANCELADO");

        assertThat(resultado.getEstado()).isEqualTo("CANCELADO");
    }

    @Test
    void guardarSinDetallesDebeCrearUnPedidoConTotalCero() {
        Pedido pedido = Pedido.builder().cliente(new Cliente()).build(); // sin detalles (null)
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = pedidoService.guardar(pedido);

        assertThat(resultado.getTotal()).isEqualTo(0.0);
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(resultado.getFechaPedido()).isNotNull();
    }

    @Test
    void guardarDebeEnlazarDetallesConElPedidoYCalcularElTotal() {
        Producto producto = Producto.builder().id(1L).nombre("Mouse").precio(50.0).stock(10).build();
        DetallePedido detalle = DetallePedido.builder()
                .cantidad(2)
                .producto(Producto.builder().id(1L).build()) // solo llega el ID desde el JSON
                .build();

        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(detalle);
        Pedido pedido = Pedido.builder().cliente(new Cliente()).detalles(detalles).build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = pedidoService.guardar(pedido);

        // El bug corregido: el detalle debe quedar enlazado a su pedido
        assertThat(detalle.getPedido()).isEqualTo(pedido);
        // El precio se toma del producto real en BD, no del que mande el cliente
        assertThat(detalle.getPrecioUnitario()).isEqualTo(50.0);
        assertThat(detalle.getSubtotal()).isEqualTo(100.0);
        assertThat(resultado.getTotal()).isEqualTo(100.0);
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
    }

    @Test
    void guardarDebeRechazarUnDetalleSinProductoValido() {
        DetallePedido detalle = DetallePedido.builder().cantidad(1).build(); // sin producto
        Pedido pedido = Pedido.builder().detalles(List.of(detalle)).build();

        assertThatThrownBy(() -> pedidoService.guardar(pedido))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void guardarDebeRechazarUnDetalleConProductoSinId() {
        // producto no es null, pero su id sí -> segunda condición del OR
        DetallePedido detalle = DetallePedido.builder().cantidad(1).producto(new Producto()).build();
        Pedido pedido = Pedido.builder().detalles(List.of(detalle)).build();

        assertThatThrownBy(() -> pedidoService.guardar(pedido))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void guardarDebeRechazarUnDetalleConCantidadNula() {
        DetallePedido detalle = DetallePedido.builder().producto(Producto.builder().id(1L).build()).build(); // cantidad null
        Pedido pedido = Pedido.builder().detalles(List.of(detalle)).build();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(Producto.builder().id(1L).precio(10.0).build()));

        assertThatThrownBy(() -> pedidoService.guardar(pedido))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void guardarDebeRechazarUnDetalleConCantidadCeroONegativa() {
        DetallePedido detalle = DetallePedido.builder().cantidad(0).producto(Producto.builder().id(1L).build()).build();
        Pedido pedido = Pedido.builder().detalles(List.of(detalle)).build();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(Producto.builder().id(1L).precio(10.0).build()));

        assertThatThrownBy(() -> pedidoService.guardar(pedido))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void procesarVentaDebeRechazarUnPedidoConDetallesNulos() {
        // Cubre la otra mitad del OR: detalles == null (no solo detalles vacío)
        Pedido pedido = Pedido.builder().id(20L).estado("PENDIENTE").detalles(null).build();
        when(repository.findById(20L)).thenReturn(Optional.of(pedido));
        PagoYape estrategia = new PagoYape();

        assertThatThrownBy(() -> pedidoService.procesarVenta(20L, "BOLETA", estrategia))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void procesarVentaDebeOrquestarInventarioPagoYComprobante() {
        Producto producto = Producto.builder().id(1L).build();
        DetallePedido detalle = DetallePedido.builder().cantidad(1).producto(producto).build();
        Pedido pedido = Pedido.builder().id(5L).total(118.0).estado("PENDIENTE")
                .detalles(new ArrayList<>(List.of(detalle))).build();

        when(repository.findById(5L)).thenReturn(Optional.of(pedido));
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        String resultado = pedidoService.procesarVenta(5L, "FACTURA", new PagoYape());

        verify(inventarioService).validarStock(1L, 1);
        verify(inventarioService).descontarStock(1L, 1);
        verify(pagoService).registrarPago(pedido, 118.0, "YAPE");
        verify(comprobanteService).generarComprobante(pedido, "FACTURA");
        assertThat(pedido.getEstado()).isEqualTo("COMPLETADO");
        assertThat(resultado).contains("YAPE");
    }

    @Test
    void procesarVentaDebeRechazarUnPedidoSinDetalles() {
        Pedido pedido = Pedido.builder().id(6L).estado("PENDIENTE").detalles(new ArrayList<>()).build();
        when(repository.findById(6L)).thenReturn(Optional.of(pedido));

        // CORRECCIÓN: Instanciar fuera de la lambda
        PagoYape metodoPago = new PagoYape();

        assertThatThrownBy(() -> pedidoService.procesarVenta(6L, "BOLETA", metodoPago))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void procesarVentaDebeRechazarUnPedidoYaCompletado() {
        DetallePedido detalle = DetallePedido.builder().cantidad(1).producto(Producto.builder().id(1L).build()).build();
        Pedido pedido = Pedido.builder().id(7L).estado("COMPLETADO").detalles(List.of(detalle)).build();
        when(repository.findById(7L)).thenReturn(Optional.of(pedido));

        // CORRECCIÓN: Instanciar fuera de la lambda
        PagoYape metodoPago = new PagoYape();

        assertThatThrownBy(() -> pedidoService.procesarVenta(7L, "BOLETA", metodoPago))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void actualizarSoloElClienteNoDebeTocarLosDetalles() {
        DetallePedido detalleExistente = DetallePedido.builder()
                .cantidad(2).precioUnitario(10.0).subtotal(20.0).producto(Producto.builder().id(1L).build()).build();
        Pedido existente = Pedido.builder().id(12L).estado("PENDIENTE")
                .total(20.0).detalles(new ArrayList<>(List.of(detalleExistente))).build();

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDni("87654321");
        Pedido cambios = Pedido.builder().cliente(nuevoCliente).build(); // sin detalles

        when(repository.findById(12L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = pedidoService.actualizar(12L, cambios);

        assertThat(resultado.getCliente()).isEqualTo(nuevoCliente);
        assertThat(resultado.getDetalles()).hasSize(1); // no se tocaron
        assertThat(resultado.getTotal()).isEqualTo(20.0); // no se recalculó
    }

    @Test
    void actualizarDebeReemplazarDetallesYRecalcularElTotal() {
        Producto producto = Producto.builder().id(1L).precio(30.0).build();
        Pedido existente = Pedido.builder().id(8L).estado("PENDIENTE").detalles(new ArrayList<>()).build();

        DetallePedido nuevoDetalle = DetallePedido.builder()
                .cantidad(3).producto(Producto.builder().id(1L).build()).build();

        Pedido cambios = Pedido.builder().detalles(new ArrayList<>(List.of(nuevoDetalle))).build();

        when(repository.findById(8L)).thenReturn(Optional.of(existente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = pedidoService.actualizar(8L, cambios);

        assertThat(resultado.getTotal()).isEqualTo(90.0); // 30.0 * 3
        assertThat(resultado.getDetalles()).hasSize(1);
    }

    @Test
    void actualizarDebeRechazarUnPedidoYaCompletado() {
        Pedido pedido = Pedido.builder().id(9L).estado("COMPLETADO").build();
        when(repository.findById(9L)).thenReturn(Optional.of(pedido));

        // CORRECCIÓN: Instanciar fuera de la lambda
        Pedido pedidoVacio = new Pedido();

        assertThatThrownBy(() -> pedidoService.actualizar(9L, pedidoVacio))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void eliminarDebeBorrarUnPedidoPendiente() {
        Pedido pedido = Pedido.builder().id(10L).estado("PENDIENTE").build();
        when(repository.findById(10L)).thenReturn(Optional.of(pedido));

        pedidoService.eliminar(10L);

        verify(repository, times(1)).delete(pedido);
    }

    @Test
    void eliminarDebeRechazarUnPedidoYaCompletado() {
        Pedido pedido = Pedido.builder().id(11L).estado("COMPLETADO").build();
        when(repository.findById(11L)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> pedidoService.eliminar(11L))
                .isInstanceOf(IllegalStateException.class);

        verify(repository, never()).delete(any(Pedido.class));
    }
}