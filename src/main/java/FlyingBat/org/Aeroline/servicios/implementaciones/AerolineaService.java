package FlyingBat.org.Aeroline.servicios.implementaciones;

import FlyingBat.org.Aeroline.modelos.Aerolinea;
import FlyingBat.org.Aeroline.servicios.interfaces.IAerolineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class AerolineaService implements IAerolineaService {

    @Autowired
    private  IAerolineaService aerolineaService;
    @Override
    public Page<Aerolinea> buscarTodosPaginados(Pageable pageable) {
        return aerolineaService.buscarTodosPaginados(pageable);
    }

    @Override
    public List<Aerolinea> obtenerTodos() {
        return aerolineaService.obtenerTodos();
    }

    @Override
    public Optional<Aerolinea> buscarPorId(Integer id) {
        return aerolineaService.buscarPorId(id);
    }

    @Override
    public Aerolinea creoOEditar(Aerolinea aerolinea) {
        return aerolineaService.creoOEditar(aerolinea);
    }

    @Override
    public void eliminarPorId(Integer id) {
        aerolineaService.eliminarPorId(id);
    }
}
