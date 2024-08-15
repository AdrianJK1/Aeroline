package FlyingBat.org.Aeroline.modelos;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    private Vuelo vuelo;

    @NotBlank(message = "Fecha de reserva requerida")
    private String fechaReserva;

    private String status;  //Activo o inactivo en compras (reserva u compra)

    private String boletoElectronico;
}
