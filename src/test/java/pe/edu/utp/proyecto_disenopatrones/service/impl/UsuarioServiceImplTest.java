package pe.edu.utp.proyecto_disenopatrones.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.proyecto_disenopatrones.model.Rol;
import pe.edu.utp.proyecto_disenopatrones.model.Usuario;
import pe.edu.utp.proyecto_disenopatrones.repository.RolRepository;
import pe.edu.utp.proyecto_disenopatrones.repository.UsuarioRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Rol rolCliente;

    @BeforeEach
    void setUp() {
        rolCliente = Rol.builder().id(2L).nombre("ROLE_CLIENTE").build();
    }

    @Test
    void registrarUsuarioDebeCrearloCuandoNoExisteElUsername() {
        // El cliente solo manda el ID del rol (como llegaría desde Swagger/JSON)
        Usuario nuevo = Usuario.builder()
                .username("jperez").password("clave123")
                .rol(Rol.builder().id(2L).build())
                .personaId(1L).build();

        when(repository.existsByUsername("jperez")).thenReturn(false);
        when(rolRepository.findById(2L)).thenReturn(Optional.of(rolCliente));
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = usuarioService.registrarUsuario(nuevo);

        assertThat(resultado.getUsername()).isEqualTo("jperez");
        // El Rol "de juguete" (solo id) debe haberse reemplazado por el Rol real de BD
        assertThat(resultado.getRol()).isEqualTo(rolCliente);
        assertThat(resultado.getRol().getNombre()).isEqualTo("ROLE_CLIENTE");
    }

    @Test
    void registrarUsuarioDebeRechazarUsernameDuplicado() {
        Usuario nuevo = Usuario.builder().username("jperez").password("clave123")
                .rol(Rol.builder().id(2L).build()).build();
        when(repository.existsByUsername("jperez")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.registrarUsuario(nuevo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe");

        verify(repository, never()).save(any());
    }

    @Test
    void registrarUsuarioDebeRechazarRolInexistente() {
        Usuario nuevo = Usuario.builder().username("nuevo").password("clave123")
                .rol(Rol.builder().id(99L).build()).build();
        when(repository.existsByUsername("nuevo")).thenReturn(false);
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.registrarUsuario(nuevo))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void registrarUsuarioDebeRechazarUsuarioSinRol() {
        Usuario nuevo = Usuario.builder().username("sinRol").password("clave123").build();
        when(repository.existsByUsername("sinRol")).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.registrarUsuario(nuevo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void registrarUsuarioDebeRechazarUsernameEnBlanco() {
        Usuario nuevo = Usuario.builder().username("   ").password("clave123").build();

        assertThatThrownBy(() -> usuarioService.registrarUsuario(nuevo))
                .isInstanceOf(IllegalArgumentException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void registrarUsuarioDebeRechazarPasswordEnBlanco() {
        Usuario nuevo = Usuario.builder().username("valido").password("").build();

        assertThatThrownBy(() -> usuarioService.registrarUsuario(nuevo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void buscarPorUsernameDebeRetornarElUsuarioCuandoExiste() {
        Usuario usuario = Usuario.builder().username("ana").password("1234").rol(rolCliente).build();
        when(repository.findByUsername("ana")).thenReturn(Optional.of(usuario));

        assertThat(usuarioService.buscarPorUsername("ana")).isEqualTo(usuario);
    }

    @Test
    void buscarPorUsernameDebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorUsername("fantasma"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void listarDebeRetornarTodosLosUsuarios() {
        Usuario usuario = Usuario.builder().username("ana").password("1234").rol(rolCliente).build();
        when(repository.findAll()).thenReturn(java.util.List.of(usuario));

        assertThat(usuarioService.listar()).hasSize(1).containsExactly(usuario);
    }

    @Test
    void cambiarPasswordDebeActualizarLaContraseniaDelUsuario() {
        Usuario usuario = Usuario.builder().username("ana").password("1234").rol(rolCliente).build();
        when(repository.findByUsername("ana")).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        usuarioService.cambiarPassword("ana", "nuevaClave");

        assertThat(usuario.getPassword()).isEqualTo("nuevaClave");
        verify(repository).save(usuario);
    }

    @Test
    void autenticarDebeRetornarTrueConCredencialesCorrectas() {
        Usuario usuario = Usuario.builder().username("ana").password("1234").rol(rolCliente).build();
        when(repository.findByUsername("ana")).thenReturn(Optional.of(usuario));

        assertThat(usuarioService.autenticar("ana", "1234")).isTrue();
    }

    @Test
    void autenticarDebeRetornarFalseConPasswordIncorrecta() {
        Usuario usuario = Usuario.builder().username("ana").password("1234").rol(rolCliente).build();
        when(repository.findByUsername("ana")).thenReturn(Optional.of(usuario));

        assertThat(usuarioService.autenticar("ana", "otraClave")).isFalse();
    }

    @Test
    void autenticarDebeRetornarFalseSiElUsuarioNoExiste() {
        when(repository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThat(usuarioService.autenticar("fantasma", "1234")).isFalse();
    }
}
