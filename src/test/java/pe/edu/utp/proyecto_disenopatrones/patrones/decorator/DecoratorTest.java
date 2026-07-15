package pe.edu.utp.proyecto_disenopatrones.patrones.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pe.edu.utp.proyecto_disenopatrones.service.patron.decorator.DescuentoDecorator;
import pe.edu.utp.proyecto_disenopatrones.service.patron.decorator.IgvDecorator;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.ComprobanteFactory;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class DecoratorTest {

    @Test
    void debeAgregarIgvYDescuentoAlComprobante() {
        log.info("=== TEST DECORATOR: Envolviendo una Boleta ===");

        // 1. Creamos la Boleta base (100 soles)
        IComprobante boletaBase = ComprobanteFactory.crear("BOLETA", 100.0);
        log.info("1. Descripcion de la boleta pura: {}", boletaBase.getDescripcion());
        log.info("2. Precio Base de la Boleta pura: S/ {}", boletaBase.calcularTotal());

        // 2. La envolvemos con el Decorador de IGV (Le suma el 18%)
        IComprobante boletaConIgv = new IgvDecorator(boletaBase);
        log.info("3. Descripcion de la boleta con igv aplicado: {}", boletaConIgv.getDescripcion());
        log.info("4. Precio tras aplicarle el Decorador IGV (18%): S/ {}", boletaConIgv.calcularTotal());

        // 3. La envolvemos con el Decorador de Descuento (Le resta 10 soles, por ejemplo)
        IComprobante boletaFinal = new DescuentoDecorator(boletaConIgv);
        log.info("5. Descripcion de la boleta con igv aplicado y su descuento: {}", boletaFinal.getDescripcion());
        log.info("6. Precio FINAL tras aplicarle el Decorador de Descuento: S/ {}", boletaFinal.calcularTotal());

        // Validaciones internas
        assertThat(boletaBase.calcularTotal()).isEqualTo(100.0);
        assertThat(boletaConIgv.calcularTotal()).isGreaterThan(100.0); // Comprueba que subió el precio
        assertThat(boletaFinal.calcularTotal()).isLessThan(boletaConIgv.calcularTotal()); // Comprueba que bajó el precio

        log.info("ÉXITO: Los decoradores modificaron el precio dinámicamente sin alterar la clase Boleta original.");
        log.info("===============================================================\n");
    }
}
