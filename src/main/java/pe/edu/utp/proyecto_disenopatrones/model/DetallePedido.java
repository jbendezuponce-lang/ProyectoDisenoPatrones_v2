package pe.edu.utp.proyecto_disenopatrones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario; // Guardamos el precio al momento exacto de la compra

    @Column(nullable = false)
    private Double subtotal;

    // @JsonBackReference: este lado NO se vuelve a serializar dentro del JSON del
    // pedido (evita el ciclo infinito Pedido -> detalles -> pedido -> detalles...).
    // @ToString/@EqualsAndHashCode.Exclude: evita el mismo ciclo infinito en
    // toString()/equals()/hashCode() generados por Lombok (usados en logs, etc.)
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}
