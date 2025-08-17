package ar.edu.utn.frbb.tup.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.ConsultaPrestamoResponseDto;
import ar.edu.utn.frbb.tup.controller.ConsultaPrestamoResponseDto.PrestamoInfo;
import ar.edu.utn.frbb.tup.controller.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.PrestamoResponseDto;
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

    public PrestamoResponseDto solicitarPrestamo(PrestamoDto prestamoDto) throws PrestamoException, CreditoRechazadoException {
        // 1. Verificar que el cliente existe
        Cliente cliente = clienteService.buscarClientePorDni(prestamoDto.getNumeroCliente());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado con DNI: " + prestamoDto.getNumeroCliente());
        }

        // 2. Convertir moneda string a enum
        TipoMoneda tipoMoneda = TipoMoneda.valueOf(prestamoDto.getMoneda().toUpperCase());

        // 3. Verificar que el cliente tiene una cuenta en la moneda solicitada
        boolean tieneCuentaEnMoneda = false;
        Cuenta cuentaDestino = null;
        for (Cuenta cuenta : cliente.getCuentas()) {
            if (cuenta.getMoneda() == tipoMoneda) {
                tieneCuentaEnMoneda = true;
                cuentaDestino = cuenta;
                break;
            }
        }

        if (!tieneCuentaEnMoneda) {
            throw new PrestamoException("El cliente no posee una cuenta en " + prestamoDto.getMoneda());
        }

        // 4. Verificar calificación crediticia
        boolean tienebuenaCalificacion = calificacionCrediticiaService.verificarCalificacionCrediticia(cliente.getDni());
        if (!tienebuenaCalificacion) {
            throw new CreditoRechazadoException("El cliente no tiene una calificación crediticia adecuada");
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

        // 10. Preparar respuesta
        PrestamoResponseDto response = new PrestamoResponseDto();
        response.setEstado("APROBADO");
        response.setMensaje("El monto del préstamo fue acreditado en su cuenta");
        response.setPlanPagos(prestamo.getPlanPagos());

        return response;
    }

    public ConsultaPrestamoResponseDto consultarPrestamos(long clienteId) {
        // Verificar que el cliente existe
        Cliente cliente = clienteService.buscarClientePorDni(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        // Obtener préstamos del cliente
        List<Prestamo> prestamos = prestamoDao.findByCliente(clienteId);

        // Preparar respuesta
        ConsultaPrestamoResponseDto response = new ConsultaPrestamoResponseDto();
        response.setNumeroCliente(clienteId);

        List<PrestamoInfo> prestamosInfo = prestamos.stream()
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
}