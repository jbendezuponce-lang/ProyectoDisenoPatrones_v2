package pe.edu.utp.proyecto_disenopatrones.service.patron.decorator;

import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;

public class DescuentoDecorator extends ComprobanteDecorator {

    public DescuentoDecorator(IComprobante comprobante) {
        super(comprobante);
    }

    @Override
    public String getDescripcion() {
        return comprobanteWrapper.getDescripcion() + " - Descuento Especial (10%)";
    }

    @Override
    public Double calcularTotal() {
        // Aplica un 10% de descuento al monto que ya traía el comprobante
        return comprobanteWrapper.calcularTotal() * 0.90;
    }
}