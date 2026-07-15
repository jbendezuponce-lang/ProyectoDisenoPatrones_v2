package pe.edu.utp.proyecto_disenopatrones.repository;
import pe.edu.utp.proyecto_disenopatrones.model.Envio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
}
