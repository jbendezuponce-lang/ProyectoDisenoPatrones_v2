package pe.edu.utp.proyecto_disenopatrones.service;

import pe.edu.utp.proyecto_disenopatrones.model.Cliente;
import java.util.List;

public interface ClienteService {
    Cliente guardar(Cliente cliente);
    List<Cliente> listar();
    Cliente buscarPorId(Long id);
    Cliente buscarPorDni(String dni);
    Cliente actualizar(Long id, Cliente cliente);
    void eliminar(Long id);
}
