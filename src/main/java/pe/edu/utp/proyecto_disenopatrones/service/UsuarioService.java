package pe.edu.utp.proyecto_disenopatrones.service;

import pe.edu.utp.proyecto_disenopatrones.model.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    Usuario buscarPorUsername(String username);
    boolean autenticar(String username, String password);
    void cambiarPassword(String username, String nuevaPassword);
    List<Usuario> listar();
}
