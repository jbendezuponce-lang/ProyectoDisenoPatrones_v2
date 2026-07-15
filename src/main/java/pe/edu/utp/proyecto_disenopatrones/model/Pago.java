package pe.edu.utp.proyecto_disenopatrones.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private String metodo; // YAPE, TARJETA, EFECTIVO (Aquí entra el Strategy)

    // Un pago pertenece a un único pedido
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}
