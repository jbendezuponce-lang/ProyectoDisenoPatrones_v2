package pe.edu.utp.proyecto_disenopatrones.patrones.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class StrategyTest {

    @Test
    void debeSeleccionarLaEstrategiaCorrectaEnTiempoDeEjecucion() {
        log.info("=== TEST STRATEGY: Cambiando métodos de pago dinámicamente ===");

        // El cliente decide pagar con Yape
        MetodoPago estrategia1 = new PagoYape();
        log.info("Escenario A - El cliente escoge pagar desde su celular.");
        log.info("El sistema enrutó el pago a la estrategia: [{}]", estrategia1.getNombre());

        // De pronto el cliente se arrepiente y saca su Tarjeta Visa
        MetodoPago estrategia2 = new PagoTarjeta();
        log.info("Escenario B - El cliente cambia de opinión e inserta su plástico.");
        log.info("El sistema enrutó el pago a la estrategia: [{}]", estrategia2.getNombre());

        MetodoPago estrategia3 = new PagoPlin();
        log.info("Escenario C - El cliente cambia de opinión e inserta desde su celular");
        log.info("El sistema enrutó el pago a la estrategia: [{}]", estrategia3.getNombre());

        MetodoPago estrategia4 = new PagoEfectivo();
        log.info("Escenario D - El cliente cambia de opinión e inserta su plástico.");
        log.info("El sistema enrutó el pago a la estrategia: [{}]", estrategia4.getNombre());

        // Verificaciones
        assertThat(estrategia1.getNombre()).containsIgnoringCase("YAPE");
        assertThat(estrategia2.getNombre()).containsIgnoringCase("TARJETA");
        assertThat(estrategia3.getNombre()).containsIgnoringCase("PLIN");
        assertThat(estrategia4.getNombre()).containsIgnoringCase("EFECTIVO");

        log.info("ÉXITO: El patrón Strategy nos permite cambiar el algoritmo de cobro al vuelo sin usar bloques 'IF-ELSE'.");
        log.info("===============================================================\n");
    }
}