package FlyingBat.org.Aeroline.repositorios;

import FlyingBat.org.Aeroline.modelos.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVueloRepository extends JpaRepository<Vuelo, Integer> {

}
