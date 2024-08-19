package FlyingBat.org.Aeroline.servicios.interfaces;

import FlyingBat.org.Aeroline.modelos.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IReservaService {

    Page<Reserva> buscarTodospaginados(Pageable pageable);

    List<Reserva> buscartodos ();

    Optional<Reserva>buscarPorId( Integer id );

     Reserva crearOEditar (Reserva reserva);

     void elimimarPorid( Integer id);

}
