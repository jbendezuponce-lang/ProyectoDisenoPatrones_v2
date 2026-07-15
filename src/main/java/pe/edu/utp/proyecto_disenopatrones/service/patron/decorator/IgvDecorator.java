package pe.edu.utp.proyecto_disenopatrones.service.patron.decorator;

import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;
import pe.edu.utp.proyecto_disenopatrones.service.patron.singleton.ConfiguracionEmpresa;

public class IgvDecorator extends ComprobanteDecorator {

    public IgvDecorator(IComprobante comprobante) {
        super(comprobante);
    }

    @Override
    public String getDescripcion() {
        return comprobanteWrapper.getDescripcion() + " + IGV Aplicado";
    }

    @Override
    public Double calcularTotal() {
        // Usamos el Singleton para traer el 18% dinámicamente
        double igv = ConfiguracionEmpresa.getInstance().getIgv();
        return comprobanteWrapper.calcularTotal() * (1 + igv);
    }
}
