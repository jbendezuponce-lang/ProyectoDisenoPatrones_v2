package pe.edu.utp.proyecto_disenopatrones.repository;
import pe.edu.utp.proyecto_disenopatrones.model.Direccion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
}