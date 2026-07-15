package pe.edu.utp.proyecto_disenopatrones;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProyectoDisenoPatronesApplicationTests {

	@Test
	void contextLoads() {
		// Prueba de humo (smoke test) intencionalmente vacía: su único propósito
		// es verificar que el ApplicationContext de Spring Boot levante sin
		// errores (beans mal configurados, propiedades inválidas, etc.).
		// Si algo falla al armar el contexto, este test falla solo, sin
		// necesitar ninguna aserción explícita.
	}

}
