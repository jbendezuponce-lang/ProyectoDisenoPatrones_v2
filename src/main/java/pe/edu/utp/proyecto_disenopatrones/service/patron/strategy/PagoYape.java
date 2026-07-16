package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PagoYape implements MetodoPago {
    @Override
    public String procesarPago(Double monto) {
        // Log del algoritmo específico
        log.info("[STRATEGY-CONCRETO] Ejecutando pasarela de cobro específica para YAPE por S/ {}", monto);
        return "Pago de S/ " + monto + " realizado exitosamente mediante Yape.";
    }

    @Override
    public String getNombre() {
        return "YAPE";
    }
}
