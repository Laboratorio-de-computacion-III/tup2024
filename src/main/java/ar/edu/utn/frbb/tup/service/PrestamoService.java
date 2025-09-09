package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoDao prestamoDao;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CalificacionCrediticiaService calificacionCrediticiaService;

    public PrestamoResponseDto solicitarPrestamo(PrestamoDto prestamoDto)
            throws PrestamoException, CreditoRechazadoException {

        // 1. Verificar que el cliente existe
        Cliente cliente = null;
        try {
            cliente = clienteService.buscarClientePorDni(prestamoDto.getNumeroCliente());
        } catch (Exception e) {
            throw new PrestamoException("Cliente no encontrado con DNI: " + prestamoDto.getNumeroCliente());
        }

        if (cliente == null) {
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
        Cuenta cuentaDestino = null;
        boolean tieneCuentas = cliente.getCuentas() != null && !cliente.getCuentas().isEmpty();

        if (!tieneCuentas) {
            throw new PrestamoException("El cliente no posee ninguna cuenta bancaria. Debe crear una cuenta en " + prestamoDto.getMoneda() + " antes de solicitar un préstamo");
        }

        for (Cuenta cuenta : cliente.getCuentas()) {
            if (cuenta.getMoneda() == tipoMoneda) {
                cuentaDestino = cuenta;
                break;
            }
        }

        if (cuentaDestino == null) {
            throw new PrestamoException("El cliente no posee una cuenta en " + prestamoDto.getMoneda() + ". Debe crear una cuenta en esta moneda antes de solicitar el préstamo");
        }

        // 4. Verificar calificación crediticia
        boolean tienebuenaCalificacion = calificacionCrediticiaService.verificarCalificacionCrediticia(cliente.getDni());
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

        // 8. Guardar el préstamo
        prestamoDao.save(prestamo);

        // 9. Actualizar la cuenta
        cuentaService.actualizarCuenta(cuentaDestino);

        // 10. Preparar respuesta exitosa
        PrestamoResponseDto response = new PrestamoResponseDto();
        response.setEstado("APROBADO");
        response.setMensaje("El monto del préstamo fue acreditado en su cuenta");
        response.setPlanPagos(prestamo.getPlanPagos());

        return response;
    }

    public ConsultaPrestamoResponseDto consultarPrestamos(long clienteId) {
        return new ConsultaPrestamoResponseDto();
    }
}