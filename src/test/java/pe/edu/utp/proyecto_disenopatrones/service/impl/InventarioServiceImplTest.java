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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow; // <-- Nueva importación para cumplir con SonarQube
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder().id(1L).nombre("Teclado").precio(80.0).stock(5).build();
    }

    @Test
    void validarStockNoDebeLanzarExcepcionSiHaySuficiente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // CORRECCIÓN: Aserción explícita de que el método no debe lanzar errores
        assertDoesNotThrow(() -> inventarioService.validarStock(1L, 3));
    }

    @Test
    void validarStockDebeLanzarExcepcionSiNoAlcanza() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> inventarioService.validarStock(1L, 100))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    @Test
    void validarStockDebeLanzarExcepcionSiProductoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventarioService.validarStock(99L, 1))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void descontarStockDebeRestarLaCantidadYGuardar() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        inventarioService.descontarStock(1L, 2);

        assertThat(producto.getStock()).isEqualTo(3);
    }

    @Test
    void aumentarStockDebeSumarLaCantidadYGuardar() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        inventarioService.aumentarStock(1L, 10);

        assertThat(producto.getStock()).isEqualTo(15);
    }

    @Test
    void consultarExistenciasDebeRetornarElStockActual() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThat(inventarioService.consultarExistencias(1L)).isEqualTo(5);
    }
}