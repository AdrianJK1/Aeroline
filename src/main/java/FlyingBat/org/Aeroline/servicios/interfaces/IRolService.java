package FlyingBat.org.Aeroline.servicios.interfaces;

import FlyingBat.org.Aeroline.modelos.Rol;

import java.util.List;
import java.util.Optional;

public interface IRolService {
    Optional<Rol> obtenerRolPorNombre(String nombre);
    List<Rol> listarRoles();
}
