package pe.edu.utp.proyecto_disenopatrones.service;

import pe.edu.utp.proyecto_disenopatrones.model.Producto;
import java.util.List;

public interface ProductoService {
    Producto guardar(Producto producto);
    List<Producto> listar();
    Producto buscarPorId(Long id);
    Producto actualizar(Long id, Producto producto);
    void eliminar(Long id);
    void actualizarStock(Long id, Integer cantidad);
}
