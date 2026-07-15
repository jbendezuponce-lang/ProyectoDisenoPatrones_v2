package pe.edu.utp.proyecto_disenopatrones.service.patron.singleton;

/**
 * Patrón SINGLETON.
 * Se justifica su uso porque la configuración de la empresa
 * (RUC, IGV, dirección) es un dato global, de solo lectura en tiempo de
 * ejecución, que debe ser el mismo para toda la aplicación (por ejemplo,
 * el IGV que usa {@code IgvDecorator} al calcular cualquier comprobante).
 */

@SuppressWarnings("java:S6548")
public class ConfiguracionEmpresa {

    private static ConfiguracionEmpresa instancia;

    // Variables globales del sistema
    private String nombreEmpresa = "NOKIA S.A.C.";
    private String ruc = "20601234567";
    private String direccion = "Av. Los Ingenieros 123, Ate";
    private double igv = 0.18; // 18%

    // Constructor privado para evitar 'new'
    private ConfiguracionEmpresa() {}

    // Método global de acceso
    public static synchronized ConfiguracionEmpresa getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionEmpresa();
        }
        return instancia;
    }

    // Getters
    public String getNombreEmpresa() { return nombreEmpresa; }
    public String getRuc() { return ruc; }
    public String getDireccion() { return direccion; }
    public double getIgv() { return igv; }
}
