package FlyingBat.org.Aeroline.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aerolinea")
public class Aerolinea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El codigo IATA es requerido")
    private String codigoIata;

    @NotBlank(message ="Numero de atencion al cliente requerido")
    private  String numeroAtencion;

    @NotBlank(message = "Tipo de vuelo requerido")
    private  String tipoVuelo;

    @OneToMany(mappedBy = "aerolinea")
    private List<Vuelo> vuelos;




}
