package pe.edu.utp.proyecto_disenopatrones.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.ComprobanteFactory;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;
import pe.edu.utp.proyecto_disenopatrones.service.patron.decorator.IgvDecorator;
import pe.edu.utp.proyecto_disenopatrones.service.patron.decorator.DescuentoDecorator;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteController {

    @PostMapping("/simular-generacion")
    public ResponseEntity<Map<String, Object>> simularFactoryYDecorator(
            @RequestParam String tipoDocumento,
            @RequestParam Double montoInicial,
            @RequestParam boolean aplicarDescuento,
            @RequestParam boolean aplicarIgv) {

        // 1. FACTORY
        IComprobante documento = ComprobanteFactory.crear(tipoDocumento, montoInicial);

        // 2. DECORATORS DINÁMICOS
        if (aplicarDescuento) {
            documento = new DescuentoDecorator(documento);
        }
        if (aplicarIgv) {
            documento = new IgvDecorator(documento);
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("descripcion_final", documento.getDescripcion());
        respuesta.put("monto_final_calculado", documento.calcularTotal());

        return ResponseEntity.ok(respuesta);
    }
}