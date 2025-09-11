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

import java.math.BigDecimal;
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
     *
     * @param prestamoDto Datos del préstamo solicitado
     * @return Respuesta de la solicitud de préstamo
     * @throws PrestamoException Si hay errores en la validación
     * @throws CreditoRechazadoException Si se rechaza por calificación crediticia
     */
    public PrestamoResponseDto solicitarPrestamo(PrestamoDto prestamoDto)
            throws PrestamoException, CreditoRechazadoException {

        Cliente cliente;
        try {
            cliente = clienteService.buscarClientePorDni(prestamoDto.getNumeroCliente());
        } catch (Exception e) {
            throw new PrestamoException("Cliente no encontrado con DNI: " + prestamoDto.getNumeroCliente());
        }

        TipoMoneda tipoMoneda;
        try {
            tipoMoneda = TipoMoneda.valueOf(prestamoDto.getMoneda().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new PrestamoException("Moneda inválida: " + prestamoDto.getMoneda() + ". Use PESOS o DOLARES");
        }

        List<Cuenta> cuentasEnMoneda = cuentaService.obtenerCuentasPorClienteYMoneda(
                cliente.getDni(), tipoMoneda);

        if (cuentasEnMoneda.isEmpty()) {
            throw new PrestamoException("El cliente no posee una cuenta en " + prestamoDto.getMoneda() +
                    ". Debe crear una cuenta en esta moneda antes de solicitar el préstamo");
        }

        Cuenta cuentaDestino = cuentasEnMoneda.get(0);

        boolean tienebuenaCalificacion = calificacionCrediticiaService
                .verificarCalificacionCrediticia(cliente.getDni());

        if (!tienebuenaCalificacion) {
            throw new CreditoRechazadoException(
                    "El cliente no tiene una calificación crediticia adecuada para este préstamo");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setNumeroCliente(cliente.getDni());
        prestamo.setMontoPrestamo(prestamoDto.getMontoPrestamo());
        prestamo.setPlazoMeses(prestamoDto.getPlazoMeses());
        prestamo.setMoneda(tipoMoneda);
        prestamo.setEstado(EstadoPrestamo.APROBADO);

        prestamo.calcularPlanPagos();

        BigDecimal monto = prestamoDto.getMontoPrestamo();
        cuentaDestino.setBalance(cuentaDestino.getBalance().add(monto));

        prestamoRepository.save(prestamo);
        cuentaService.actualizarCuenta(cuentaDestino);

        PrestamoResponseDto response = new PrestamoResponseDto();
        response.setEstado("APROBADO");
        response.setMensaje("El monto del préstamo fue acreditado en su cuenta");
        response.setPlanPagos(prestamo.getPlanPagos());

        return response;
    }

    /**
     * Consulta los préstamos de un cliente
     *
     * @param clienteId DNI del cliente
     * @return Información de préstamos del cliente
     * @throws IllegalArgumentException Si el cliente no existe
     */
    @Transactional(readOnly = true)
    public ConsultaPrestamoResponseDto consultarPrestamos(long clienteId) {

        if (!clienteService.existeCliente(clienteId)) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        List<Prestamo> prestamos = prestamoRepository.findByNumeroCliente(clienteId);

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
     * Actualiza un préstamo existente
     *
     * @param prestamo Préstamo a actualizar
     * @return Préstamo actualizado
     */
    public Prestamo actualizarPrestamo(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }
}