package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

public class PagoPlin implements MetodoPago {
    @Override
    public String procesarPago(Double monto) {
        return "Pago de S/ " + monto + " realizado exitosamente mediante Plin.";
    }

    @Override
    public String getNombre() {
        return "PLIN";
    }
}
