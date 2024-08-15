package FlyingBat.org.Aeroline.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "vuelo")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Oringen de vuelo requerido")
    private String origen;

    @NotBlank(message = "Destido de vuelo requerido")
    private String destino;

    @NotBlank(message = "Fecha/hora salida requerida")
    private String fechaHorasalida;

    @NotBlank(message = "Fecha/hora llegada requerida")
    private String fechaHorallegada;

    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;
}
