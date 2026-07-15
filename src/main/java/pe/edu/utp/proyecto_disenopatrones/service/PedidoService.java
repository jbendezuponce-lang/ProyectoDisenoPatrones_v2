package pe.edu.utp.proyecto_disenopatrones.service;

import pe.edu.utp.proyecto_disenopatrones.model.Pedido;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.MetodoPago;
import java.util.List;

public interface PedidoService {
    Pedido guardar(Pedido pedido);
    List<Pedido> listar();
    Pedido buscarPorId(Long id);
    Pedido actualizarEstado(Long id, String nuevoEstado);
    Pedido actualizar(Long id, Pedido pedido);
    void eliminar(Long id);

    // Este método debe existir aquí para que el Controller no marque error
    String procesarVenta(Long pedidoId, String tipoDocumento, MetodoPago metodoPago);
}
