package pe.edu.utp.proyecto_disenopatrones.service;

import pe.edu.utp.proyecto_disenopatrones.model.Comprobante;
import pe.edu.utp.proyecto_disenopatrones.model.Pedido;

public interface ComprobanteService {
    Comprobante generarComprobante(Pedido pedido, String tipo);
    Comprobante buscarPorNumero(String numeroEmision);
}