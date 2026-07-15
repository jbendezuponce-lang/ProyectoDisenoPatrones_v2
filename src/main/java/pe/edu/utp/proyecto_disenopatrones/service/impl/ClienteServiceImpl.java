package pe.edu.utp.proyecto_disenopatrones.service.impl;

import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Cliente;
import pe.edu.utp.proyecto_disenopatrones.repository.ClienteRepository;
import pe.edu.utp.proyecto_disenopatrones.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Implementación de {@link pe.edu.utp.proyecto_disenopatrones.service.ClienteService}.
 * Gestiona el alta y consulta de clientes (registro, listado, búsqueda por
 * ID y por DNI). No aplica un patrón de diseño GoF directamente, pero es
 * consumido por {@link PedidoServiceImpl} al crear un pedido.
 */
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    @Override
    @Transactional
    public Cliente guardar(Cliente cliente) {
        log.info("Guardando cliente con DNI: {}", cliente.getDni());
        // No se envuelve la excepción: así el GlobalExceptionHandler puede distinguir
        // un DNI duplicado (DataIntegrityViolationException) y dar un mensaje claro,
        // en vez de un 500 genérico.
        return repository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        log.info("Listando clientes");
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        log.info("Buscando cliente por ID: {}", id);
        return obtenerClienteDesdeBD(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorDni(String dni) {
        log.info("Buscando cliente por DNI: {}", dni);
        return repository.findByDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente con DNI " + dni + " no encontrado"));
    }

    /**
     * Método interno privado sin anotaciones (@Transactional) para evitar el
     * "Problema de Auto-Invocación de Proxies" detectado por SonarQube.
     */
    private Cliente obtenerClienteDesdeBD(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
    }

    @Override
    @Transactional
    public Cliente actualizar(Long id, Cliente cliente) {
        log.info("Actualizando cliente ID: {}", id);

        // Se llama al método privado para evitar romper el proxy @Transactional
        Cliente existente = obtenerClienteDesdeBD(id);

        existente.setNombres(cliente.getNombres());
        existente.setApellidos(cliente.getApellidos());
        existente.setDni(cliente.getDni());
        existente.setCorreo(cliente.getCorreo());
        existente.setTelefono(cliente.getTelefono());
        existente.setRuc(cliente.getRuc());
        existente.setDireccionEntrega(cliente.getDireccionEntrega());

        return repository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando cliente con ID: {}", id);

        // Se llama al método privado para evitar romper el proxy @Transactional
        Cliente cliente = obtenerClienteDesdeBD(id);
        repository.delete(cliente);
    }
}