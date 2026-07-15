package pe.edu.utp.proyecto_disenopatrones.service.patron.factory;

public class Factura implements IComprobante {
    private Double montoBase;

    public Factura(Double montoBase) {
        this.montoBase = montoBase;
    }

    @Override
    public String getDescripcion() {
        return "Factura Electrónica";
    }

    @Override
    public Double calcularTotal() {
        return montoBase;
    }
}