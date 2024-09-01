package FlyingBat.org.Aeroline.servicios.interfaces;

import FlyingBat.org.Aeroline.modelos.Vuelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface IVueloService {

    Page<Vuelo> obtenerTodosPaginados (Pageable pageable);

    List<Vuelo> listarVuelos();

    List<Vuelo> obtenerTodos();

    Optional<Vuelo> obtenerPorId (Integer id);

    Vuelo crearOEditar (Vuelo vuelo);

    void eliminarPorId (Integer id);

}
