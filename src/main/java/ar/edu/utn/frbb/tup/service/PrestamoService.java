package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ConsultaPrestamoResponseDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.EstadoPrestamo;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CreditoRechazadoException;
import ar.edu.utn.frbb.tup.model.exception.PrestamoException;
import ar.edu.utn.frbb.tup.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CalificacionCrediticiaService calificacionCrediticiaService;

    /**
     * Procesa una solicitud de préstamo
     * @param prestamoDto Datos del préstamo solicitado
     * @return Respuesta de la solicitud de préstamo
     * @throws PrestamoException Si hay errores en la validación
     * @throws CreditoRechazadoException Si se rechaza por calificación crediticia
     */
    public PrestamoResponseDto solicitarPrestamo(PrestamoDto prestamoDto)
            throws PrestamoException, CreditoRechazadoException {

        // 1. Verificar que el cliente existe
        Cliente cliente;
        try {
            cliente = clienteService.buscarClientePorDni(prestamoDto.getNumeroCliente());
        } catch (Exception e) {
            throw new PrestamoException("Cliente no encontrado con DNI: " + prestamoDto.getNumeroCliente());
        }

        // 2. Convertir moneda string a enum
        TipoMoneda tipoMoneda;
        try {
            tipoMoneda = TipoMoneda.valueOf(prestamoDto.getMoneda().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PrestamoException("Moneda inválida: " + prestamoDto.getMoneda() + ". Use PESOS o DOLARES");
        }

        // 3. Verificar que el cliente tiene una cuenta en la moneda solicitada
        List<Cuenta> cuentasEnMoneda = cuentaService.obtenerCuentasPorClienteYMoneda(
                cliente.getDni(), tipoMoneda);

        if (cuentasEnMoneda.isEmpty()) {
            throw new PrestamoException("El cliente no posee una cuenta en " + prestamoDto.getMoneda() +
                    ". Debe crear una cuenta en esta moneda antes de solicitar el préstamo");
        }

        // Usar la primera cuenta encontrada en esa moneda
        Cuenta cuentaDestino = cuentasEnMoneda.get(0);

        // 4. Verificar calificación crediticia
        boolean tienebuenaCalificacion = calificacionCrediticiaService
                .verificarCalificacionCrediticia(cliente.getDni());

        if (!tienebuenaCalificacion) {
            throw new CreditoRechazadoException("El cliente no tiene una calificación crediticia adecuada para este préstamo");
        }

        // 5. Crear el préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setNumeroCliente(cliente.getDni());
        prestamo.setMontoPrestamo(prestamoDto.getMontoPrestamo());
        prestamo.setPlazoMeses(prestamoDto.getPlazoMeses());
        prestamo.setMoneda(tipoMoneda);
        prestamo.setEstado(EstadoPrestamo.APROBADO);

        // 6. Calcular plan de pagos
        prestamo.calcularPlanPagos();

        // 7. Acreditar el monto en la cuenta del cliente
        int montoEnCentavos = (int)(prestamoDto.getMontoPrestamo() * 100);
        cuentaDestino.setBalance(cuentaDestino.getBalance() + montoEnCentavos);

        // 8. Guardar el préstamo y actualizar la cuenta
        prestamoRepository.save(prestamo);
        cuentaService.actualizarCuenta(cuentaDestino);

        // 9. Preparar respuesta exitosa
        PrestamoResponseDto response = new PrestamoResponseDto();
        response.setEstado("APROBADO");
        response.setMensaje("El monto del préstamo fue acreditado en su cuenta");
        response.setPlanPagos(prestamo.getPlanPagos());

        return response;
    }

    /**
     * Consulta los préstamos de un cliente
     * @param clienteId DNI del cliente
     * @return Información de préstamos del cliente
     * @throws IllegalArgumentException Si el cliente no existe
     */
    @Transactional(readOnly = true)
    public ConsultaPrestamoResponseDto consultarPrestamos(long clienteId) {
        // Verificar que el cliente existe
        if (!clienteService.existeCliente(clienteId)) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        // Obtener préstamos del cliente
        List<Prestamo> prestamos = prestamoRepository.findByNumeroCliente(clienteId);

        // Preparar respuesta
        ConsultaPrestamoResponseDto response = new ConsultaPrestamoResponseDto();
        response.setNumeroCliente(clienteId);

        List<ConsultaPrestamoResponseDto.PrestamoInfo> prestamosInfo = prestamos.stream()
                .map(p -> new ConsultaPrestamoResponseDto.PrestamoInfo(
                        p.getMontoPrestamo(),
                        p.getPlazoMeses(),
                        p.getPagosRealizados(),
                        p.getSaldoRestante()
                ))
                .collect(Collectors.toList());

        response.setPrestamos(prestamosInfo);
        return response;
    }

    /**
     * Obtiene todos los préstamos de un cliente por estado
     * @param clienteId DNI del cliente
     * @param estado Estado del préstamo
     * @return Lista de préstamos filtrados por estado
     */
    @Transactional(readOnly = true)
    public List<Prestamo> obtenerPrestamosPorEstado(long clienteId, EstadoPrestamo estado) {
        return prestamoRepository.findByNumeroClienteAndEstado(clienteId, estado);
    }

    /**
     * Obtiene préstamos activos de un cliente
     * @param clienteId DNI del cliente
     * @return Lista de préstamos activos
     */
    @Transactional(readOnly = true)
    public List<Prestamo> obtenerPrestamosActivos(long clienteId) {
        return prestamoRepository.findPrestamosActivosByCliente(clienteId);
    }

    /**
     * Calcula el monto total prestado a un cliente
     * @param clienteId DNI del cliente
     * @return Monto total prestado
     */
    @Transactional(readOnly = true)
    public Double calcularMontoTotalPrestado(long clienteId) {
        return prestamoRepository.getTotalMontoPrestadoByCliente(clienteId);
    }

    /**
     * Calcula el saldo total pendiente de un cliente
     * @param clienteId DNI del cliente
     * @return Saldo total pendiente
     */
    @Transactional(readOnly = true)
    public Double calcularSaldoTotalPendiente(long clienteId) {
        return prestamoRepository.getTotalSaldoPendienteByCliente(clienteId);
    }

    /**
     * Busca un préstamo por ID
     * @param prestamoId ID del préstamo
     * @return Préstamo encontrado o null
     */
    @Transactional(readOnly = true)
    public Prestamo buscarPrestamoPorId(long prestamoId) {
        return prestamoRepository.findById(prestamoId).orElse(null);
    }

    /**
     * Actualiza un préstamo existente
     * @param prestamo Préstamo a actualizar
     * @return Préstamo actualizado
     */
    public Prestamo actualizarPrestamo(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }
}