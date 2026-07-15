package pe.edu.utp.proyecto_disenopatrones.patrones.factory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.Boleta;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.ComprobanteFactory;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.Factura;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class ComprobanteFactoryTest {

    @Test
    void debeCrearUnaFacturaCuandoElTipoEsFactura() {
        log.info("=== TEST FACTORY 1: Solicitando una FACTURA ===");

        // Le pedimos a la fábrica que nos haga una factura base de 100 soles
        IComprobante comprobante = ComprobanteFactory.crear("FACTURA", 100.0);

        log.info("1. Tipo de documento entregado: {}", comprobante.getDescripcion());
        log.info("2. Clase Java real en memoria: {}", comprobante.getClass().getSimpleName());
        log.info("3. MONTO TOTAL CALCULADO POR LA FACTURA: S/ {}", comprobante.calcularTotal());

        // Verificaciones invisibles para SonarQube
        assertThat(comprobante).isInstanceOf(Factura.class);
        assertThat(comprobante.calcularTotal()).isEqualTo(100.0);

        log.info("ÉXITO: La fábrica creó correctamente la Factura y sus cálculos funcionan.");
        log.info("===============================================================\n");
    }

    @Test
    void debeCrearUnaBoletaCuandoElTipoEsBoleta() {
        log.info("=== TEST FACTORY 2: Solicitando una BOLETA ===");

        // Le pedimos a la fábrica que nos haga una boleta base de 50 soles
        IComprobante comprobante = ComprobanteFactory.crear("BOLETA", 50.0);

        log.info("1. Tipo de documento entregado: {}", comprobante.getDescripcion());
        log.info("2. Clase Java real en memoria: {}", comprobante.getClass().getSimpleName());
        log.info("3. MONTO TOTAL CALCULADO POR LA BOLETA: S/ {}", comprobante.calcularTotal());

        // Verificaciones invisibles para SonarQube
        assertThat(comprobante).isInstanceOf(Boleta.class);
        assertThat(comprobante.calcularTotal()).isEqualTo(50.0);

        log.info("ÉXITO: La fábrica construyó y devolvió la Boleta adecuadamente.");
        log.info("===============================================================\n");
    }

    @Test
    void debeRechazarUnTipoInvalido() {
        log.info("=== TEST FACTORY 3: Solicitando un documento INVENTADO ===");
        log.info("Intentando crear un comprobante de tipo: 'TICKET_TREN' ");

        assertThatThrownBy(() -> ComprobanteFactory.crear("TICKET_TREN", 10.0))
                .isInstanceOf(IllegalArgumentException.class);

        log.info("ÉXITO: La fábrica bloqueó la solicitud y lanzó un error, protegiendo el sistema.");
        log.info("===============================================================\n");
    }
}