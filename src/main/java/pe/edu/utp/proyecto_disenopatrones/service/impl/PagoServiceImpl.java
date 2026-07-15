package pe.edu.utp.proyecto_disenopatrones.service.impl;


import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Pago;
import pe.edu.utp.proyecto_disenopatrones.model.Pedido;
import pe.edu.utp.proyecto_disenopatrones.repository.PagoRepository;
import pe.edu.utp.proyecto_disenopatrones.util.FechaUtil;
import pe.edu.utp.proyecto_disenopatrones.service.PagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Implementación de {@link pe.edu.utp.proyecto_disenopatrones.service.PagoService}.
 * Persiste el resultado de un pago ya procesado por el patrón <b>Strategy</b>
 * ({@code ProcesadorPago} + {@code MetodoPago}): este servicio no decide CÓMO
 * se cobra (esa lógica vive en las estrategias), solo registra el pago en BD.
 */
public class PagoServiceImpl implements PagoService {

    private final PagoRepository repository;

    @Override
    @Transactional
    public Pago registrarPago(Pedido pedido, Double monto, String metodo) {
        log.info("Registrando pago de {} via {} para el pedido {}", monto, metodo, pedido.getId());
        Pago pago = Pago.builder()
                .pedido(pedido)
                .monto(monto)
                .metodo(metodo)
                .fechaPago(FechaUtil.ahora())
                .build();
        return repository.save(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public Pago buscarPorPedidoId(Long pedidoId) {
        log.info("Buscando pago para pedido ID: {}", pedidoId);
        return repository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un pago registrado para el pedido " + pedidoId));
    }
}
