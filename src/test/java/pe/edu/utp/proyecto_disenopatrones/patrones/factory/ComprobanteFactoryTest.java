package pe.edu.utp.proyecto_disenopatrones.patrones.factory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.Boleta;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.ComprobanteFactory;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.Factura;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas del patrón FACTORY METHOD (creacional).
 * Valida que {@link ComprobanteFactory} construya el tipo de producto
 * correcto (Boleta o Factura) según el parámetro recibido, y que TODAS
 * las validaciones de entrada (tipo nulo/en blanco/inválido, monto
 * nulo/negativo) funcionen correctamente.
 */
@Slf4j
class ComprobanteFactoryTest {

    @Test
    void debeCrearUnaFacturaCuandoElTipoEsFactura() {
        IComprobante comprobante = ComprobanteFactory.crear("FACTURA", 100.0);

        assertThat(comprobante).isInstanceOf(Factura.class);
        assertThat(comprobante.getDescripcion()).isEqualTo("Factura Electrónica");
        assertThat(comprobante.calcularTotal()).isEqualTo(100.0);
    }

    @Test
    void debeCrearUnaBoletaCuandoElTipoEsBoleta() {
        IComprobante comprobante = ComprobanteFactory.crear("BOLETA", 50.0);

        assertThat(comprobante).isInstanceOf(Boleta.class);
        assertThat(comprobante.getDescripcion()).isEqualTo("Boleta Electrónica");
        assertThat(comprobante.calcularTotal()).isEqualTo(50.0);
    }

    @Test
    void debeSerInsensibleAMayusculasYMinusculas() {
        assertThat(ComprobanteFactory.crear("factura", 10.0)).isInstanceOf(Factura.class);
        assertThat(ComprobanteFactory.crear("boleta", 10.0)).isInstanceOf(Boleta.class);
    }

    @Test
    void debeRechazarUnTipoInvalido() {
        assertThatThrownBy(() -> ComprobanteFactory.crear("TICKET_TREN", 10.0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void debeRechazarTipoNulo() {
        assertThatThrownBy(() -> ComprobanteFactory.crear(null, 10.0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void debeRechazarTipoEnBlanco() {
        assertThatThrownBy(() -> ComprobanteFactory.crear("   ", 10.0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void debeRechazarMontoNulo() {
        assertThatThrownBy(() -> ComprobanteFactory.crear("BOLETA", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void debeRechazarMontoNegativo() {
        assertThatThrownBy(() -> ComprobanteFactory.crear("BOLETA", -5.0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
