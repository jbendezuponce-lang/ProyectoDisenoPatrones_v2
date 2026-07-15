package pe.edu.utp.proyecto_disenopatrones.service.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReporteServiceImplTest {

    private final ReporteServiceImpl reporteService = new ReporteServiceImpl();

    @Test
    void generarReporteVentasDiariasDebeRetornarUnMensajeDeExito() {
        assertThat(reporteService.generarReporteVentasDiarias()).contains("Reporte de Ventas");
    }

    @Test
    void generarReporteStockBajoDebeRetornarUnMensajeDeExito() {
        assertThat(reporteService.generarReporteStockBajo()).contains("Reporte de Inventario");
    }
}
