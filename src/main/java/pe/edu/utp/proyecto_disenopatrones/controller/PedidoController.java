package pe.edu.utp.proyecto_disenopatrones.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.proyecto_disenopatrones.model.Pedido;
import pe.edu.utp.proyecto_disenopatrones.service.PedidoService;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.MetodoPago;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.PagoEfectivo;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.PagoTarjeta;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.PagoYape;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.PagoPlin;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@SuppressWarnings("java:S4684")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedidoBasico(@RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.guardar(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.actualizar(id, pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Este es el endpoint maestro donde ocurre la venta real
    @PostMapping("/{id}/procesar-venta")
    public ResponseEntity<String> procesarVentaCompleta(
            @PathVariable Long id,
            @RequestParam String tipoComprobante,
            @RequestParam String metodoPagoStr) {

        // Convertir el texto a la clase Strategy correspondiente
        MetodoPago estrategia;
        switch (metodoPagoStr.toUpperCase()) {
            case "YAPE": estrategia = new PagoYape(); break;
            case "TARJETA": estrategia = new PagoTarjeta(); break;
            case "PLIN": estrategia = new PagoPlin(); break;
            default: estrategia = new PagoEfectivo(); break;
        }

        // Se invoca el servicio que coordina la venta completa (stock + pago + comprobante)
        String resultado = pedidoService.procesarVenta(id, tipoComprobante, estrategia);

        return ResponseEntity.ok(resultado);
    }
}
