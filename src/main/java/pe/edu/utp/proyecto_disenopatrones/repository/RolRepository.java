package pe.edu.utp.proyecto_disenopatrones.repository;
import pe.edu.utp.proyecto_disenopatrones.model.Rol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}
