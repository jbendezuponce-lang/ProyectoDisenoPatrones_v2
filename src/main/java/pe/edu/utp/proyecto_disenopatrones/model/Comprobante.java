package pe.edu.utp.proyecto_disenopatrones.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comprobantes")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroEmision; // Ej: F001-0000123

    @Column(nullable = false)
    private String tipoDocumento; // BOLETA o FACTURA (Aquí entra el Factory)

    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    @Column(nullable = false)
    private Double total;

    // Un comprobante se emite por un único pedido
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}
