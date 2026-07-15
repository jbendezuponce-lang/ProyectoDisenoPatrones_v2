package pe.edu.utp.proyecto_disenopatrones.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "empleados")
public class Empleado extends Persona {

    @Column(nullable = false)
    private String cargo; // Ej: VENDEDOR, ALMACENERO, ADMINISTRADOR

    @Column(nullable = false)
    private Double salario;
}
