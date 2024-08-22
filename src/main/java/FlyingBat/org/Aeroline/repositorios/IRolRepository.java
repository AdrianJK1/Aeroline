package FlyingBat.org.Aeroline.repositorios;

import FlyingBat.org.Aeroline.modelos.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRolRepository  extends JpaRepository <Rol, Integer> {
    Optional<Rol> findByNombre(String nombre);
}
