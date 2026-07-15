package pe.edu.utp.proyecto_disenopatrones.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Pago;
import pe.edu.utp.proyecto_disenopatrones.model.Pedido;
import pe.edu.utp.proyecto_disenopatrones.repository.PagoRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceImplTest {

    @Mock
    private PagoRepository repository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @Test
    void registrarPagoDebeGuardarConLosDatosCorrectos() {
        Pedido pedido = Pedido.builder().id(1L).build();
        when(repository.save(any(Pago.class))).thenAnswer(inv -> inv.getArgument(0));

        Pago resultado = pagoService.registrarPago(pedido, 150.0, "YAPE");

        ArgumentCaptor<Pago> captor = ArgumentCaptor.forClass(Pago.class);
        verify(repository).save(captor.capture());

        assertThat(captor.getValue().getMonto()).isEqualTo(150.0);
        assertThat(captor.getValue().getMetodo()).isEqualTo("YAPE");
        assertThat(captor.getValue().getPedido()).isEqualTo(pedido);
        assertThat(resultado.getMetodo()).isEqualTo("YAPE");
    }

    @Test
    void buscarPorPedidoIdDebeRetornarElPagoCuandoExiste() {
        Pago pago = Pago.builder().id(1L).monto(50.0).metodo("PLIN").build();
        when(repository.findByPedidoId(10L)).thenReturn(Optional.of(pago));

        assertThat(pagoService.buscarPorPedidoId(10L)).isEqualTo(pago);
    }

    @Test
    void buscarPorPedidoIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findByPedidoId(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagoService.buscarPorPedidoId(10L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}

