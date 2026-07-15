package pe.edu.utp.proyecto_disenopatrones.service.patron.factory;

public class ComprobanteFactory {

    private ComprobanteFactory() {
        // Clase de utilidad (Factory Method estático): no debe instanciarse.
    }

    public static IComprobante crear(String tipo, Double monto) {
        if (monto == null || monto < 0) {
            throw new IllegalArgumentException("El monto del comprobante debe ser un número positivo");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Debes indicar el tipo de comprobante: BOLETA o FACTURA");
        }

        if (tipo.equalsIgnoreCase("FACTURA")) {
            return new Factura(monto);
        }
        if (tipo.equalsIgnoreCase("BOLETA")) {
            return new Boleta(monto);
        }
        throw new IllegalArgumentException("Tipo de comprobante inválido: '" + tipo + "'. Usa BOLETA o FACTURA.");
    }
}
