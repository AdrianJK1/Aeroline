package FlyingBat.org.Aeroline.servicios.interfaces;

import FlyingBat.org.Aeroline.modelos.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    Usuario guardarUsuario(Usuario usuario);
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    List<Usuario> listarUsuarios();
}
