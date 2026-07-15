package pe.edu.utp.proyecto_disenopatrones.service.impl;

import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.model.Rol;
import pe.edu.utp.proyecto_disenopatrones.model.Usuario;
import pe.edu.utp.proyecto_disenopatrones.repository.RolRepository;
import pe.edu.utp.proyecto_disenopatrones.repository.UsuarioRepository;
import pe.edu.utp.proyecto_disenopatrones.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de {@link UsuarioService}.
 * Gestiona el registro y autenticación de usuarios del sistema.
 * <p>
 * El registro recibe la entidad {@link Usuario} tal cual la envía el
 * cliente (con un {@link Rol} que solo trae el {@code id}), pero antes de
 * guardar SIEMPRE se reemplaza ese {@code Rol} por la instancia real y
 * persistida que existe en la base de datos (ver {@link #registrarUsuario}).
 * Esto evita el error clásico de Hibernate "TransientPropertyValueException /
 * violación de FK" que ocurre cuando se intenta guardar una relación
 * @ManyToOne con un objeto que no está gestionado por JPA.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final RolRepository rolRepository;

    @Override
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        log.info("Registrando nuevo usuario en el sistema: {}", usuario.getUsername());

        if (usuario.getUsername() == null || usuario.getUsername().isBlank()
                || usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("username y password son obligatorios");
        }
        if (repository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el username: " + usuario.getUsername());
        }
        if (usuario.getRol() == null || usuario.getRol().getId() == null) {
            throw new IllegalArgumentException(
                    "Debes indicar un rol existente, ej: \"rol\": {\"id\": 2} (1=ROLE_ADMIN, 2=ROLE_CLIENTE)");
        }

        // Se reemplaza el Rol "de juguete" que manda el cliente (solo con id)
        // por el Rol real y persistido en BD. Sin este paso, Hibernate falla
        // al intentar guardar una relación a un objeto no gestionado.
        Rol rolReal = rolRepository.findById(usuario.getRol().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Rol con ID " + usuario.getRol().getId() + " no encontrado"));
        usuario.setRol(rolReal);
        usuario.setId(null); // por seguridad: nunca dejar que el cliente fuerce un ID al registrar

        return repository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        log.info("Buscando usuario: {}", username);
        return repository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario '" + username + "' no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean autenticar(String username, String password) {
        log.info("Intento de autenticación para: {}", username);
        return repository.findByUsername(username)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    @Transactional
    public void cambiarPassword(String username, String nuevaPassword) {
        log.info("Solicitud de cambio de contraseña para: {}", username);

        // CORRECCIÓN PARA SONARQUBE: Se llama directamente al repository
        // para evitar la auto-invocación a métodos con @Transactional
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario '" + username + "' no encontrado"));

        usuario.setPassword(nuevaPassword);
        repository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        log.info("Listando todos los usuarios registrados en el sistema");
        return repository.findAll();
    }
}