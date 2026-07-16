package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PagoTarjeta implements MetodoPago {
    @Override
    public String procesarPago(Double monto) {
        // Log del algoritmo específico
        log.info("[STRATEGY-CONCRETO] Ejecutando pasarela de cobro específica para TARJETA por S/ {}", monto);
        return "Pago de S/ " + monto + " validado y procesado mediante Tarjeta de Crédito/Débito.";
    }

    @Override
    public String getNombre() {
        return "TARJETA";
    }
}
