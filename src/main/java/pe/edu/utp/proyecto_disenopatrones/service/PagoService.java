package pe.edu.utp.proyecto_disenopatrones.service;

import pe.edu.utp.proyecto_disenopatrones.model.Pago;
import pe.edu.utp.proyecto_disenopatrones.model.Pedido;

public interface PagoService {
    Pago registrarPago(Pedido pedido, Double monto, String metodo);
    Pago buscarPorPedidoId(Long pedidoId);
}