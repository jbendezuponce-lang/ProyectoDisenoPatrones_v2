package pe.edu.utp.proyecto_disenopatrones.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.utp.proyecto_disenopatrones.util.FechaUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Captura TODAS las excepciones de la aplicación y las convierte en una
 * respuesta JSON clara con el status HTTP correcto, en vez del error 500
 * en blanco (Whitelabel Error Page) que mostraba Spring por defecto.
 * Esto es lo que hacía que en Swagger los POST "no funcionaran": sí se
 * ejecutaban, pero el error real quedaba oculto.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> cuerpo(HttpStatus status, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", FechaUtil.ahora());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", mensaje);
        return body;
    }

    // 404: el recurso (cliente, producto, pedido, etc.) no existe
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNoEncontrado(RecursoNoEncontradoException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 400: violación de restricciones de BD (FK inexistente, campo único duplicado, NOT NULL, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegridad(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad de datos: {}", ex.getMessage());
        String mensaje = "Datos inválidos: revisa que los IDs referenciados (cliente, producto, categoria, rol, etc.) "
                + "existan en la base de datos y que no estés duplicando un valor único (ej. DNI, RUC, username).";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo(HttpStatus.BAD_REQUEST, mensaje));
    }

    // 400: JSON mal formado o de tipo incorrecto en el body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonInvalido(HttpMessageNotReadableException ex) {
        log.warn("JSON inválido en el request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(cuerpo(HttpStatus.BAD_REQUEST, "El cuerpo (body) enviado no es un JSON válido para este recurso."));
    }

    // 400: errores de validación @Valid en DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacion(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                sb.append(err.getField()).append(": ").append(err.getDefaultMessage()).append(". "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo(HttpStatus.BAD_REQUEST, sb.toString()));
    }

    // 400: errores de negocio esperados (stock insuficiente, usuario ya existe, credenciales, etc.)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NegocioException.class})
    public ResponseEntity<Map<String, Object>> handleNegocio(RuntimeException ex) {
        log.warn("Error de regla de negocio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 500: cualquier otra excepción no controlada (última red de seguridad)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenerico(Exception ex) {
        log.error("Error no controlado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(cuerpo(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado: " + ex.getMessage()));
    }
}
