package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PagoEfectivo implements MetodoPago {
    @Override
    public String procesarPago(Double monto) {
        // Log del algoritmo específico
        log.info("[STRATEGY-CONCRETO] Ejecutando pasarela de cobro específica para PAGO EFECTIVO por S/ {}", monto);
        return "Pago de S/ " + monto + " registrado. Pendiente de cobro en caja (Efectivo).";
    }

    @Override
    public String getNombre() {
        return "EFECTIVO";
    }
}
