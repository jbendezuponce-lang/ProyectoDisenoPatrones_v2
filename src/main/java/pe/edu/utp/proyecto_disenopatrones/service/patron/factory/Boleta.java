package pe.edu.utp.proyecto_disenopatrones.service.patron.factory;

public class Boleta implements IComprobante {
    private Double montoBase;

    public Boleta(Double montoBase) {
        this.montoBase = montoBase;
    }

    @Override
    public String getDescripcion() {
        return "Boleta Electrónica";
    }

    @Override
    public Double calcularTotal() {
        return montoBase;
    }
}