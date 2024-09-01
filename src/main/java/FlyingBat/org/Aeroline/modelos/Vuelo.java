package FlyingBat.org.Aeroline.modelos;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vuelo")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Oringen de vuelo requerido")
    private String origen;

    @NotBlank(message = "Destido de vuelo requerido")
    private String destino;

    private LocalDateTime fechaHorasalida;

    private LocalDateTime fechaHorallegada;

    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;



}

