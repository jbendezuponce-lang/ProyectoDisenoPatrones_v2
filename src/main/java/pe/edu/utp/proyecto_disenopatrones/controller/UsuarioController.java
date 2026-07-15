package pe.edu.utp.proyecto_disenopatrones.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.proyecto_disenopatrones.model.Usuario;
import pe.edu.utp.proyecto_disenopatrones.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@SuppressWarnings("java:S4684")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Registra un usuario nuevo.
     * Body de ejemplo en Swagger:
     * {@code { "username": "jperez", "password": "clave123", "rol": {"id": 2}, "personaId": 1 } }
     * (roles ya sembrados en data.sql: 1 = ROLE_ADMIN, 2 = ROLE_CLIENTE)
     */
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        boolean autenticado = usuarioService.autenticar(username, password);

        Map<String, String> respuesta = new HashMap<>();
        if (autenticado) {
            respuesta.put("mensaje", "Login exitoso. Bienvenido " + username);
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("error", "Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listar());
    }
}
