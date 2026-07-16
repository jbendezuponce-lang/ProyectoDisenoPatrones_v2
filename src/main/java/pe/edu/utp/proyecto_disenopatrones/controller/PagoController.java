package pe.edu.utp.proyecto_disenopatrones.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.proyecto_disenopatrones.service.patron.strategy.*;
import java.util.HashMap;
import java.util.Map;

@RestController

@Tag(name = "Pagos, patron strategy", description = "Operaciones relacionadas con el procesamiento de pagos usando Strategy")
@RequestMapping("/api/pagos")
public class PagoController {

    @PostMapping("/procesar")
    public ResponseEntity<Map<String, String>> procesarPagoStrategy(
            @RequestParam Double monto,
            @RequestParam String metodo) {

        MetodoPago estrategiaPago;

        // Selección dinámica de la estrategia
        switch (metodo.toUpperCase()) {
            case "YAPE": estrategiaPago = new PagoYape(); break;
            case "TARJETA": estrategiaPago = new PagoTarjeta(); break;
            case "PLIN": estrategiaPago = new PagoPlin(); break;
            default: estrategiaPago = new PagoEfectivo(); break;
        }

        // Ejecución del patrón
        ProcesadorPago procesador = new ProcesadorPago(estrategiaPago);
        String resultado = procesador.procesar(monto);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("metodo_seleccionado", metodo);
        respuesta.put("resultado_strategy", resultado);

        return ResponseEntity.ok(respuesta);
    }
}
