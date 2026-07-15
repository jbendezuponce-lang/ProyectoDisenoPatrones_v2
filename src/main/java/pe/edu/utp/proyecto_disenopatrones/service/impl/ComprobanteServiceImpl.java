package pe.edu.utp.proyecto_disenopatrones.service.impl;

import pe.edu.utp.proyecto_disenopatrones.exception.RecursoNoEncontradoException;
import pe.edu.utp.proyecto_disenopatrones.util.FechaUtil;
import pe.edu.utp.proyecto_disenopatrones.model.Comprobante;
import pe.edu.utp.proyecto_disenopatrones.model.Pedido;
import pe.edu.utp.proyecto_disenopatrones.repository.ComprobanteRepository;
import pe.edu.utp.proyecto_disenopatrones.service.ComprobanteService;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.ComprobanteFactory;
import pe.edu.utp.proyecto_disenopatrones.service.patron.factory.IComprobante;
import pe.edu.utp.proyecto_disenopatrones.service.patron.decorator.IgvDecorator;
import pe.edu.utp.proyecto_disenopatrones.service.patron.decorator.DescuentoDecorator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Implementación de {@link pe.edu.utp.proyecto_disenopatrones.service.ComprobanteService}.
 * Orquesta la emisión de comprobantes de pago combinando tres patrones de diseño:
 * <ul>
 *   <li><b>Factory Method</b>: {@code ComprobanteFactory} crea la Boleta o Factura base.</li>
 *   <li><b>Decorator</b>: {@code DescuentoDecorator} e {@code IgvDecorator} envuelven
 *       el comprobante para sumar descuento e IGV sin modificar sus clases.</li>
 *   <li><b>Singleton</b>: el IGV se obtiene de {@code ConfiguracionEmpresa.getInstance()}
 *       dentro del {@code IgvDecorator}.</li>
 * </ul>
 */
public class ComprobanteServiceImpl implements ComprobanteService {

    private final ComprobanteRepository repository;

    @Override
    @Transactional
    public Comprobante generarComprobante(Pedido pedido, String tipo) {

        // 1. PATRÓN FACTORY: Creamos la Boleta o Factura base
        IComprobante documentoPatron = ComprobanteFactory.crear(tipo, pedido.getTotal());

        // 2. PATRÓN DECORATOR: el descuento especial solo aplica a Factura (regla de negocio),
        // pero el IGV (vía Singleton) aplica siempre, sea Boleta o Factura.
        if (tipo.equalsIgnoreCase("FACTURA")) {
            documentoPatron = new DescuentoDecorator(documentoPatron);
        }
        documentoPatron = new IgvDecorator(documentoPatron);

        log.info("Documento generado con patrones: {}", documentoPatron.getDescripcion());

        Comprobante comprobante = Comprobante.builder()
                .pedido(pedido)
                .tipoDocumento(tipo.toUpperCase())
                .numeroEmision("DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .total(documentoPatron.calcularTotal()) // Usamos el total matemático calculado por el Decorator
                .fechaEmision(FechaUtil.ahora())
                .build();
        return repository.save(comprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public Comprobante buscarPorNumero(String numeroEmision) {
        log.info("Buscando comprobante por número de emisión: {}", numeroEmision);
        return repository.findByNumeroEmision(numeroEmision)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comprobante " + numeroEmision + " no encontrado"));
    }
}