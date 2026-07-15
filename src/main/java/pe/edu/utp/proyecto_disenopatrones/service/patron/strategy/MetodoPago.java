package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

public interface MetodoPago {
    String procesarPago(Double monto);
    // Nombre legible del método de pago, usado para persistir en BD (ej: "YAPE", "TARJETA")
    String getNombre();
}
