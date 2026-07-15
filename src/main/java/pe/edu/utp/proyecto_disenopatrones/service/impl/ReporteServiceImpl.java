package pe.edu.utp.proyecto_disenopatrones.service.impl;

import pe.edu.utp.proyecto_disenopatrones.service.ReporteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
/**
 * Implementación de {@link pe.edu.utp.proyecto_disenopatrones.service.ReporteService}.
 * Genera reportes textuales simples (ventas diarias, stock bajo). Punto de
 * extensión sugerido para aplicar el patrón <b>Builder</b> a futuro si los
 * reportes crecen en complejidad (varias secciones, formatos, filtros).
 */
public class ReporteServiceImpl implements ReporteService {

    @Override
    public String generarReporteVentasDiarias() {
        log.info("Generando reporte de ventas diarias...");
        // Aquí podrás aplicar tu patrón Builder en el futuro
        return "Reporte de Ventas generado con éxito.";
    }

    @Override
    public String generarReporteStockBajo() {
        log.info("Generando reporte de stock crítico...");
        return "Reporte de Inventario generado.";
    }
}