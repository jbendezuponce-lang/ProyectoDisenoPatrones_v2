package pe.edu.utp.proyecto_disenopatrones.repository;
import pe.edu.utp.proyecto_disenopatrones.model.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}