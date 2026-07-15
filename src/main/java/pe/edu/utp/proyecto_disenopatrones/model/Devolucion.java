package pe.edu.utp.proyecto_disenopatrones.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "devoluciones")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaDevolucion;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private String estado; // PENDIENTE, APROBADA, RECHAZADA

    // Una devolución se hace sobre un Pedido específico
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}
