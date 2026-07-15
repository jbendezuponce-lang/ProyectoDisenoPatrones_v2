package pe.edu.utp.proyecto_disenopatrones.service;

public interface InventarioService {
    void validarStock(Long productoId, Integer cantidadRequerida);
    void descontarStock(Long productoId, Integer cantidad);
    void aumentarStock(Long productoId, Integer cantidad);
    Integer consultarExistencias(Long productoId);
}