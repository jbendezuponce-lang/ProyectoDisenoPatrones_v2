package pe.edu.utp.proyecto_disenopatrones.repository;

import pe.edu.utp.proyecto_disenopatrones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}
