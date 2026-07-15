package pe.edu.utp.proyecto_disenopatrones.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Producto;
import pe.edu.utp.proyecto_disenopatrones.repository.ProductoRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder().id(1L).nombre("Mouse").precio(50.0).stock(10).build();
    }

    @Test
    void guardarDebeDelegarEnElRepositorio() {
        when(repository.save(producto)).thenReturn(producto);
        assertThat(productoService.guardar(producto)).isEqualTo(producto);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productoService.buscarPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void eliminarDebeBuscarYLuegoBorrarElProducto() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.eliminar(1L);

        verify(repository, times(1)).delete(producto);
    }

    @Test
    void actualizarStockDebeModificarYGuardarElNuevoValor() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        when(repository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        productoService.actualizarStock(1L, 25);

        assertThat(producto.getStock()).isEqualTo(25);
        verify(repository).save(producto);
    }

    @Test
    void actualizarDebeModificarTodosLosCamposDelProducto() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        when(repository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto cambios = Producto.builder()
                .nombre("Mouse Gamer").descripcion("RGB").precio(89.9).stock(20).build();

        Producto resultado = productoService.actualizar(1L, cambios);

        assertThat(resultado.getNombre()).isEqualTo("Mouse Gamer");
        assertThat(resultado.getPrecio()).isEqualTo(89.9);
        assertThat(resultado.getStock()).isEqualTo(20);
    }
}
