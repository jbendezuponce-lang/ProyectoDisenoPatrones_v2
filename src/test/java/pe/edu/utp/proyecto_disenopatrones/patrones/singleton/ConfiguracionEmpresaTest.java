package pe.edu.utp.proyecto_disenopatrones.patrones.singleton;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import pe.edu.utp.proyecto_disenopatrones.service.patron.singleton.ConfiguracionEmpresa;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas del patrón SINGLETON (creacional).
 */
@Slf4j // Agregamos lombok para poder usar log.info()
class ConfiguracionEmpresaTest {

    @Test
    void debeRetornarSiempreLaMismaInstancia() {
        log.info("=== TEST SINGLETON 1: Verificando que sea el mismo objeto ===");
        // El usuario 1 pide la configuración
        ConfiguracionEmpresa instancia1 = ConfiguracionEmpresa.getInstance();
        // El usuario 2 pide la configuración
        ConfiguracionEmpresa instancia2 = ConfiguracionEmpresa.getInstance();

        // Imprimimos el código de espacio en memoria de ambas variables
        log.info("Espacio en memoria de Instancia 1: {}", instancia1.hashCode());
        log.info("Espacio en memoria de Instancia 2: {}", instancia2.hashCode());

        // Verificamos que sean exactamente la misma
        assertThat(instancia1).isSameAs(instancia2);

        log.info("ÉXITO: Las dos variables apuntan exactamente al mismo espacio de memoria.");
        log.info("===============================================================\n");
    }

    @Test
    void debeExponerLosValoresDeConfiguracionDeLaEmpresa() {
        log.info("=== TEST SINGLETON 2: Verificando los datos de la empresa ===");

        ConfiguracionEmpresa instancia = ConfiguracionEmpresa.getInstance();

        log.info("Empresa leída: {}", instancia.getNombreEmpresa());
        log.info("RUC leído: {}", instancia.getRuc());
        log.info("RUC leído: {}", instancia.getDireccion());
        log.info("IGV leído: {}", instancia.getIgv());

        log.info("ÉXITO: El Singleton devuelve la información corporativa correcta.");
        log.info("===============================================================\n");
    }
}