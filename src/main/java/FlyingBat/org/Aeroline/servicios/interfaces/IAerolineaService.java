package FlyingBat.org.Aeroline.servicios.interfaces;

import FlyingBat.org.Aeroline.modelos.Aerolinea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IAerolineaService {
    Page<Aerolinea> buscarTodosPaginados(Pageable pageable);

    List<Aerolinea> obtenerTodos();

    Optional<Aerolinea> buscarPorId(Integer id);

    Aerolinea creoOEditar(Aerolinea aerolinea);

    void eliminarPorId(Integer id);

    Aerolinea findByName(String nombre);

    Aerolinea findByTipoVuelo(String vuelo);
}
