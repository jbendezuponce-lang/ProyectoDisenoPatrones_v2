package pe.edu.utp.proyecto_disenopatrones.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.proyecto_disenopatrones.service.patron.singleton.ConfiguracionEmpresa;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerConfiguracionSingleton() {
        // Llamamos al Singleton
        ConfiguracionEmpresa config = ConfiguracionEmpresa.getInstance();

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("instancia_memoria_hash", config.hashCode()); // Esto probará que es la misma instancia
        respuesta.put("empresa", config.getNombreEmpresa());
        respuesta.put("ruc", config.getRuc());
        respuesta.put("igv_porcentaje", config.getIgv() * 100 + "%");

        return ResponseEntity.ok(respuesta);
    }
}