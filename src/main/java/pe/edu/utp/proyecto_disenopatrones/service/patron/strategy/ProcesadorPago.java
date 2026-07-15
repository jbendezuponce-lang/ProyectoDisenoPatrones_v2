package pe.edu.utp.proyecto_disenopatrones.service.patron.strategy;

public class ProcesadorPago {

    // Almacena la estrategia seleccionada (Yape, Tarjeta, etc.)
    private MetodoPago estrategia;

    // Constructor que recibe la estrategia
    public ProcesadorPago(MetodoPago estrategia) {
        this.estrategia = estrategia;
    }

    // Método que ejecuta el pago sin importar qué estrategia sea
    public String procesar(Double monto) {
        return estrategia.procesarPago(monto);
    }
}