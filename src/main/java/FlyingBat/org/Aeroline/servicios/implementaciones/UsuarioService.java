package FlyingBat.org.Aeroline.servicios.implementaciones;

import FlyingBat.org.Aeroline.modelos.Usuario;
import FlyingBat.org.Aeroline.repositorios.IUsuarioRepository;
import FlyingBat.org.Aeroline.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService  implements IUsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}
