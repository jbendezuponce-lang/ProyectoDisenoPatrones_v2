package pe.edu.utp.proyecto_disenopatrones.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente extends Persona {

    @Column(unique = true, length = 11)
    private String ruc; // Por si pide Factura en la compra

    private String direccionEntrega;
}
