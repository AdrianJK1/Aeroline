package FlyingBat.org.Aeroline.repositorios;

import FlyingBat.org.Aeroline.modelos.Vuelo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface IVueloRepository extends JpaRepository<Vuelo, Integer> {
    @EntityGraph(attributePaths = "aerolinea")
    Optional<Vuelo> findById(Integer id);
}
