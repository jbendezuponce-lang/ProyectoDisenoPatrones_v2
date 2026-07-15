package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

public class PagoTarjeta implements MetodoPago {
    @Override
    public String procesarPago(Double monto) {
        return "Pago de S/ " + monto + " validado y procesado mediante Tarjeta de Crédito/Débito.";
    }

    @Override
    public String getNombre() {
        return "TARJETA";
    }
}
