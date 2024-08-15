package FlyingBat.org.Aeroline.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "aerolinea")
public class Aerolinea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El codigo IATA es requerido")
    private String codigoIata;

    @OneToMany(mappedBy = "aerolinea")
    private List<Vuelo> vuelos;

}
