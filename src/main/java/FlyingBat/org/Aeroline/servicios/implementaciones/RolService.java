package FlyingBat.org.Aeroline.servicios.implementaciones;

import FlyingBat.org.Aeroline.modelos.Rol;
import FlyingBat.org.Aeroline.repositorios.IRolRepository;
import FlyingBat.org.Aeroline.servicios.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService implements IRolService {

    @Autowired
    private IRolRepository rolRepository;

    @Override
    public Optional<Rol> obtenerRolPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Override
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }
}
