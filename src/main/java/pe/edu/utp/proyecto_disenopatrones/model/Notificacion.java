package pe.edu.utp.proyecto_disenopatrones.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private String tipo; // CORREO, SMS, SISTEMA

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    // Se asocia a un Usuario (el que recibe la notificación)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}