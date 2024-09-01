package FlyingBat.org.Aeroline.servicios.implementaciones;


import FlyingBat.org.Aeroline.modelos.Vuelo;

import FlyingBat.org.Aeroline.repositorios.IVueloRepository;
import FlyingBat.org.Aeroline.servicios.interfaces.IVueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VueloService implements IVueloService {
    @Autowired
    private IVueloRepository vueloRepository;

    @Override
    public List<Vuelo> listarVuelos() {
        return vueloRepository.findAll();
    }

    @Override
    public Page<Vuelo> obtenerTodosPaginados(Pageable pageable) {
        return vueloRepository.findAll(pageable);
    }

    @Override
    public List<Vuelo> obtenerTodos() {
        return vueloRepository.findAll();
    }

    @Override
    public Optional<Vuelo> obtenerPorId(Integer id) {
        return vueloRepository.findById(id);
    }

    @Override
    public Vuelo crearOEditar(Vuelo vuelo) {
        return vueloRepository.save(vuelo);
    }

    @Override
    public void eliminarPorId(Integer id) {
        vueloRepository.deleteById(id);
    }


}
