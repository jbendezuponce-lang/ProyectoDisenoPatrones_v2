package pe.edu.utp.proyecto_disenopatrones.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.proyecto_disenopatrones.service.ReporteService;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas-diarias")
    public ResponseEntity<Map<String, String>> obtenerReporteVentas() {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("reporte", reporteService.generarReporteVentasDiarias());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/stock-critico")
    public ResponseEntity<Map<String, String>> obtenerReporteStock() {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("reporte", reporteService.generarReporteStockBajo());
        return ResponseEntity.ok(respuesta);
    }
}
