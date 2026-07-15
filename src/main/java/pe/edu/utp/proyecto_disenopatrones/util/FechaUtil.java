package pe.edu.utp.proyecto_disenopatrones.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utilidad centralizada para obtener la fecha/hora actual con una zona
 * horaria explícita (regla SonarQube java:S8688: los métodos ".now()" deben
 * especificar un {@link ZoneId} o {@link java.time.Clock}, ya que sin eso el
 * resultado depende silenciosamente de la zona horaria del servidor donde
 * corra la aplicación).
 * <p>
 * Se centraliza aquí en vez de repetir {@code ZoneId.of(...)} en cada
 * service, para tener un único lugar donde cambiar la zona horaria del
 * negocio si el proyecto se despliega en otro país.
 */
public final class FechaUtil {

    public static final ZoneId ZONA_HORARIA_PERU = ZoneId.of("America/Lima");

    private FechaUtil() {
        // Clase de utilidad: no debe instanciarse.
    }

    /** Fecha y hora actual en la zona horaria del negocio (America/Lima). */
    public static LocalDateTime ahora() {
        return LocalDateTime.now(ZONA_HORARIA_PERU);
    }
}
