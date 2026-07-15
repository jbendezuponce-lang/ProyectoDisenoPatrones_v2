package pe.edu.utp.proyecto_disenopatrones.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String direccionDestino;

    private LocalDateTime fechaDespacho;

    private LocalDateTime fechaEntregaEstimada;

    @Column(nullable = false)
    private String estado; // PREPARANDO, EN_RUTA, ENTREGADO

    // Un envío corresponde a un pedido
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}