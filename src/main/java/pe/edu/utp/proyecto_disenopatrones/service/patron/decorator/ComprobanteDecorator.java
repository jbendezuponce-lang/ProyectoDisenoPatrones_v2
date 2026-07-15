package pe.edu.utp.proyecto_disenopatrones.service.patron.decorator;

import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;

public abstract class ComprobanteDecorator implements IComprobante {

    // El objeto original que vamos a "envolver" y decorar
    protected IComprobante comprobanteWrapper;

    protected ComprobanteDecorator(IComprobante comprobante) {
        this.comprobanteWrapper = comprobante;
    }

    @Override
    public String getDescripcion() {
        return comprobanteWrapper.getDescripcion();
    }

    @Override
    public Double calcularTotal() {
        return comprobanteWrapper.calcularTotal();
    }
}
