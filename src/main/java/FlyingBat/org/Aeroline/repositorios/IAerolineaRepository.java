package FlyingBat.org.Aeroline.repositorios;

import FlyingBat.org.Aeroline.modelos.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IAerolineaRepository extends JpaRepository<Aerolinea, Integer> {
    Aerolinea findByNombre(String nombre);
    Aerolinea findByTipoVuelo(String TipoVuelo);
}

