package pe.edu.utp.proyecto_disenopatrones.exception;

/**
 * Excepción específica para violaciones de reglas de negocio (ej. stock
 * insuficiente, un reporte que no pudo generarse, etc.), en vez de lanzar
 * una {@link RuntimeException} genérica (regla SonarQube java:S112: nunca
 * se deben lanzar excepciones genéricas, porque el código que la captura
 * no puede distinguirla de cualquier otro error inesperado del sistema).
 */
public class NegocioException extends RuntimeException {
    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
