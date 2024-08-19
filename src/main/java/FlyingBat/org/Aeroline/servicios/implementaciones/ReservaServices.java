package FlyingBat.org.Aeroline.servicios.implementaciones;

import FlyingBat.org.Aeroline.modelos.Reserva;
import FlyingBat.org.Aeroline.repositorios.IReservaRepository;
import FlyingBat.org.Aeroline.servicios.interfaces.IReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ReservaServices implements IReservaService {
     @Autowired
     private IReservaRepository reservaRepository;
    @Override
    public Page<Reserva> buscarTodospaginados(Pageable pageable) {
        return reservaRepository.findAll( pageable );

    }

    @Override
    public List<Reserva> buscartodos() {
        return reservaRepository.findAll();
    }

    @Override
    public Optional<Reserva> buscarPorId(Integer id) {
        return reservaRepository.findById(id );
    }

    @Override
    public Reserva crearOEditar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public void elimimarPorid(Integer id) {
        reservaRepository.deleteById(id );
    }
}
