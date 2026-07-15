package pe.edu.utp.proyecto_disenopatrones.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Comprobante;
import pe.edu.utp.proyecto_disenopatrones.model.Pedido;
import pe.edu.utp.proyecto_disenopatrones.repository.ComprobanteRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.*;

/**
 * Prueba de integración de servicio: valida que ComprobanteServiceImpl
 * combine correctamente los patrones FACTORY (crear Boleta/Factura),
 * DECORATOR (Descuento + IGV) y, transitivamente, SINGLETON (IGV desde
 * ConfiguracionEmpresa) al generar un comprobante real.
 */
@ExtendWith(MockitoExtension.class)
class ComprobanteServiceImplTest {

    @Mock
    private ComprobanteRepository repository;

    @InjectMocks
    private ComprobanteServiceImpl comprobanteService;

    @Test
    void generarComprobanteFacturaDebeAplicarDescuentoYIgv() {
        Pedido pedido = Pedido.builder().id(1L).total(100.0).build();
        when(repository.save(any(Comprobante.class))).thenAnswer(inv -> inv.getArgument(0));

        Comprobante resultado = comprobanteService.generarComprobante(pedido, "FACTURA");

        ArgumentCaptor<Comprobante> captor = ArgumentCaptor.forClass(Comprobante.class);
        verify(repository).save(captor.capture());

        // 100 -> 90 (descuento 10%) -> 90*1.18 = 106.2 (IGV)
        assertThat(captor.getValue().getTotal()).isCloseTo(106.2, within(0.001));
        assertThat(captor.getValue().getTipoDocumento()).isEqualTo("FACTURA");
        assertThat(resultado.getNumeroEmision()).startsWith("DOC-");
    }

    @Test
    void generarComprobanteBoletaDebeAplicarSoloIgvSinDescuento() {
        Pedido pedido = Pedido.builder().id(2L).total(100.0).build();
        when(repository.save(any(Comprobante.class))).thenAnswer(inv -> inv.getArgument(0));

        Comprobante resultado = comprobanteService.generarComprobante(pedido, "BOLETA");

        // 100 * 1.18 = 118.0 (sin descuento, solo IGV)
        assertThat(resultado.getTotal()).isCloseTo(118.0, within(0.001));
        assertThat(resultado.getTipoDocumento()).isEqualTo("BOLETA");
    }

    @Test
    void buscarPorNumeroDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findByNumeroEmision("DOC-XXXX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> comprobanteService.buscarPorNumero("DOC-XXXX"))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}
