package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j // 1. Añadimos logs al procesador
public class ProcesadorPago {

    // Almacena la estrategia seleccionada (Yape, Tarjeta, etc.)
    private MetodoPago estrategia;

    // Constructor que recibe la estrategia
    public ProcesadorPago(MetodoPago estrategia) {
        this.estrategia = estrategia;
    }

    // Método que ejecuta el pago sin importar qué estrategia sea
    public String procesar(Double monto) {
        // Log para ver la delegación del patrón
        log.info("[PATRÓN STRATEGY] ProcesadorPago detecta solicitud. Delegando cobro de S/ {} a la estrategia: {}",
                monto, estrategia.getClass().getSimpleName());

        return estrategia.procesarPago(monto);
    }
}