package pe.edu.utp.proyecto_disenopatrones.patrones.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pe.edu.utp.proyecto_disenopatrones.service.patron.decorator.DescuentoDecorator;
import pe.edu.utp.proyecto_disenopatrones.service.patron.decorator.IgvDecorator;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.Boleta;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.Factura;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Pruebas del patrón DECORATOR (estructural).
 * Valida que {@link DescuentoDecorator} e {@link IgvDecorator} agreguen
 * comportamiento al {@link IComprobante} envuelto SIN modificar su clase
 * original, que la descripción se delegue correctamente al objeto envuelto,
 * y que sea posible apilar varios decoradores (composición dinámica).
 */
@Slf4j
class DecoratorTest {

    @Test
    void debeAgregarIgvYDescuentoAlComprobante() {
        IComprobante boletaBase = new Boleta(100.0);
        IComprobante boletaConIgv = new IgvDecorator(boletaBase);
        IComprobante boletaFinal = new DescuentoDecorator(boletaConIgv);

        assertThat(boletaBase.calcularTotal()).isEqualTo(100.0);
        assertThat(boletaConIgv.calcularTotal()).isGreaterThan(100.0);
        assertThat(boletaFinal.calcularTotal()).isLessThan(boletaConIgv.calcularTotal());
    }

    @Test
    void descuentoDecoratorDebeAplicar10PorCientoDeDescuento() {
        IComprobante factura = new Factura(100.0);
        IComprobante conDescuento = new DescuentoDecorator(factura);

        assertThat(conDescuento.calcularTotal()).isCloseTo(90.0, within(0.001));
    }

    @Test
    void igvDecoratorDebeAgregar18PorCientoUsandoElSingleton() {
        IComprobante boleta = new Boleta(100.0);
        IComprobante conIgv = new IgvDecorator(boleta);

        assertThat(conIgv.calcularTotal()).isCloseTo(118.0, within(0.001));
    }

    @Test
    void getDescripcionDebeDelegarseAlComprobanteEnvuelto() {
        // Cubre ComprobanteDecorator.getDescripcion(): cada decorador concreto
        // llama a super.getDescripcion() (clase base) y le agrega su propio
        // sufijo, en vez de duplicar la lógica de delegación.
        IComprobante factura = new Factura(100.0);
        IComprobante decorado = new IgvDecorator(new DescuentoDecorator(factura));

        String esperado = factura.getDescripcion() + " - Descuento Especial (10%)" + " + IGV Aplicado";
        assertThat(decorado.getDescripcion()).isEqualTo(esperado);
    }

    @Test
    void debePermitirApilarVariosDecoradoresEnCualquierOrden() {
        IComprobante factura = new Factura(100.0);
        IComprobante decorado = new IgvDecorator(new DescuentoDecorator(factura));

        // 100 -> 90 (descuento) -> 90 * 1.18 = 106.2 (igv)
        assertThat(decorado.calcularTotal()).isCloseTo(106.2, within(0.001));
    }

    @Test
    void noDebeAlterarElComprobanteOriginalEnvuelto() {
        IComprobante facturaOriginal = new Factura(100.0);
        new DescuentoDecorator(facturaOriginal);

        assertThat(facturaOriginal.calcularTotal()).isEqualTo(100.0);
    }
}