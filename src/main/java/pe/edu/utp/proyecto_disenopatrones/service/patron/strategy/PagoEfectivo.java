package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

public class PagoEfectivo implements MetodoPago {
    @Override
    public String procesarPago(Double monto) {
        return "Pago de S/ " + monto + " registrado. Pendiente de cobro en caja (Efectivo).";
    }

    @Override
    public String getNombre() {
        return "EFECTIVO";
    }
}
