package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PagoPlin implements MetodoPago {
    @Override
    public String procesarPago(Double monto) {
        // Log del algoritmo específico
        log.info("[STRATEGY-CONCRETO] Ejecutando pasarela de cobro específica para PLIN por S/ {}", monto);
        return "Pago de S/ " + monto + " realizado exitosamente mediante Plin.";
    }

    @Override
    public String getNombre() {
        return "PLIN";
    }
}
