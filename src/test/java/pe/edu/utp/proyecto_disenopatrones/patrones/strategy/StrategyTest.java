package pe.edu.utp.proyecto_disenopatrones.patrones.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas del patrón STRATEGY (comportamiento).
 * Valida que {@link ProcesadorPago} (el contexto) delegue correctamente en
 * cada estrategia concreta (Yape, Tarjeta, Plin, Efectivo) sin conocer sus
 * detalles internos, y que cada estrategia sea intercambiable en tiempo
 * de ejecución.
 */
@Slf4j
class StrategyTest {

    @Test
    void debeSeleccionarLaEstrategiaCorrectaEnTiempoDeEjecucion() {
        log.info("=== TEST STRATEGY: Cambiando métodos de pago dinámicamente ===");

        MetodoPago estrategia1 = new PagoYape();
        MetodoPago estrategia2 = new PagoTarjeta();
        MetodoPago estrategia3 = new PagoPlin();
        MetodoPago estrategia4 = new PagoEfectivo();

        assertThat(estrategia1.getNombre()).containsIgnoringCase("YAPE");
        assertThat(estrategia2.getNombre()).containsIgnoringCase("TARJETA");
        assertThat(estrategia3.getNombre()).containsIgnoringCase("PLIN");
        assertThat(estrategia4.getNombre()).containsIgnoringCase("EFECTIVO");

        log.info("ÉXITO: cada estrategia expone su nombre correctamente.");
    }

    @Test
    void procesadorPagoDebeDelegarEnLaEstrategiaYape() {
        // Ejercita ProcesadorPago (el contexto del patrón) + PagoYape.procesarPago()
        ProcesadorPago procesador = new ProcesadorPago(new PagoYape());

        String resultado = procesador.procesar(100.0);

        assertThat(resultado).contains("Yape");
    }

    @Test
    void procesadorPagoDebeDelegarEnLaEstrategiaTarjeta() {
        ProcesadorPago procesador = new ProcesadorPago(new PagoTarjeta());
        assertThat(procesador.procesar(50.0)).contains("Tarjeta");
    }

    @Test
    void procesadorPagoDebeDelegarEnLaEstrategiaPlin() {
        ProcesadorPago procesador = new ProcesadorPago(new PagoPlin());
        assertThat(procesador.procesar(50.0)).contains("Plin");
    }

    @Test
    void procesadorPagoDebeDelegarEnLaEstrategiaEfectivo() {
        ProcesadorPago procesador = new ProcesadorPago(new PagoEfectivo());
        assertThat(procesador.procesar(50.0)).contains("Efectivo");
    }

    @Test
    void debePoderIntercambiarLaEstrategiaEnTiempoDeEjecucion() {
        MetodoPago[] estrategias = { new PagoYape(), new PagoTarjeta(), new PagoPlin(), new PagoEfectivo() };

        for (MetodoPago estrategia : estrategias) {
            ProcesadorPago procesador = new ProcesadorPago(estrategia);
            assertThat(procesador.procesar(20.0)).isNotBlank();
        }
    }
}
