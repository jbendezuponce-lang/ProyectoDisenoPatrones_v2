package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

public class PagoYape implements MetodoPago {
    @Override
    public String procesarPago(Double monto) {
        return "Pago de S/ " + monto + " realizado exitosamente mediante Yape.";
    }

    @Override
    public String getNombre() {
        return "YAPE";
    }
}
