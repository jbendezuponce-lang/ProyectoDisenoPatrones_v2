package pe.edu.utp.proyecto_disenopatrones.repository;
import pe.edu.utp.proyecto_disenopatrones.model.Almacen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
}