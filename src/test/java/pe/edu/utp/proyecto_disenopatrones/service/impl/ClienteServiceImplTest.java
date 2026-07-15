package pe.edu.utp.proyecto_disenopatrones.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Cliente;
import pe.edu.utp.proyecto_disenopatrones.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setDni("12345678");
        cliente.setNombres("Juan");
    }

    @Test
    void guardarDebeDelegarEnElRepositorio() {
        when(repository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.guardar(cliente);

        assertThat(resultado).isEqualTo(cliente);
        verify(repository, times(1)).save(cliente);
    }

    @Test
    void listarDebeRetornarTodosLosClientes() {
        when(repository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteService.listar();

        assertThat(resultado).hasSize(1).containsExactly(cliente);
    }

    @Test
    void buscarPorIdDebeRetornarElClienteCuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorId(1L);

        assertThat(resultado.getDni()).isEqualTo("12345678");
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void buscarPorDniDebeRetornarElClienteCuandoExiste() {
        when(repository.findByDni("12345678")).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorDni("12345678");

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void buscarPorDniDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findByDni("00000000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorDni("00000000"))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void actualizarDebeModificarLosDatosDelClienteExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        when(repository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente cambios = new Cliente();
        cambios.setNombres("Juan Carlos");
        cambios.setApellidos("Perez");
        cambios.setDni("87654321");
        cambios.setRuc("20123456789");

        Cliente resultado = clienteService.actualizar(1L, cambios);

        assertThat(resultado.getNombres()).isEqualTo("Juan Carlos");
        assertThat(resultado.getDni()).isEqualTo("87654321");
        assertThat(resultado.getRuc()).isEqualTo("20123456789");
    }

    @Test
    void actualizarDebeLanzarExcepcionSiElClienteNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // CORRECCIÓN: Se instancia el objeto fuera del lambda
        Cliente clienteVacio = new Cliente();

        assertThatThrownBy(() -> clienteService.actualizar(99L, clienteVacio))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void eliminarDebeBuscarYLuegoBorrarElCliente() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.eliminar(1L);

        verify(repository, times(1)).delete(cliente);
    }
}