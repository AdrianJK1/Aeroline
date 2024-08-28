package FlyingBat.org.Aeroline.servicios.implementaciones;

import FlyingBat.org.Aeroline.modelos.Aerolinea;
import FlyingBat.org.Aeroline.repositorios.IAerolineaRepository;
import FlyingBat.org.Aeroline.servicios.interfaces.IAerolineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AerolineaService implements IAerolineaService {

    @Autowired
    private IAerolineaRepository aerolineaService;
    @Override
    public Page<Aerolinea> buscarTodosPaginados(Pageable pageable) {
        return aerolineaService.findAll(pageable);
    }

    @Override
    public List<Aerolinea> obtenerTodos() {
        return aerolineaService.findAll();
    }

    @Override
    public Optional<Aerolinea> buscarPorId(Integer id) {
        return aerolineaService.findById(id);
    }

    @Override
    public Aerolinea creoOEditar(Aerolinea aerolinea) {
        return aerolineaService.save(aerolinea);
    }

    @Override
    public void eliminarPorId(Integer id) {
        aerolineaService.deleteById(id);
    }

    @Override
    public Aerolinea findByName(String nombre){
        return aerolineaService.findByNombre(nombre);
    }

    @Override
    public Aerolinea findByTipoVuelo(String vuelo){
        return aerolineaService.findByTipoVuelo(vuelo);
    }

}
