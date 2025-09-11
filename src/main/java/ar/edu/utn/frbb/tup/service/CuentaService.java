package ar.edu.utn.frbb.tup.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.utn.frbb.tup.controller.PrestamoController;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.repository.CuentaRepository;

@Service
@Transactional
public class CuentaService {

    private static final Logger log = LoggerFactory.getLogger(PrestamoController.class);

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteService clienteService;

    /**
     * Da de alta una nueva cuenta
     *
     * @param cuenta Cuenta a crear
     * @param dniTitular DNI del titular
     * @throws CuentaAlreadyExistsException Si la cuenta ya existe
     * @throws TipoCuentaAlreadyExistsException Si el cliente ya tiene una cuenta del mismo tipo y moneda
     */
    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular)
            throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException {

        if (cuentaRepository.existsById(cuenta.getNumeroCuenta())) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        if (cuentaRepository.existsByTitularDniAndTipoCuentaAndMoneda(
                dniTitular, cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException(
                    "El cliente ya posee una cuenta " + cuenta.getTipoCuenta() + " en " + cuenta.getMoneda());
        }

        validarTipoCuentaSoportada(cuenta.getTipoCuenta(), cuenta.getMoneda());
        this.asociarTitular(cuenta, dniTitular);
        cuentaRepository.save(cuenta);

        clienteService.asociarCuenta(cuenta, dniTitular);

        log.info("Cuenta creada exitosamente: {}", cuenta);
    }

    /**
     * Actualiza una cuenta existente
     *
     * @param cuenta Cuenta a actualizar
     */
    public void actualizarCuenta(Cuenta cuenta) {
        cuentaRepository.save(cuenta);
    }

    /**
     * Obtiene todas las cuentas de un cliente
     *
     * @param dni DNI del cliente
     * @return Lista de cuentas del cliente
     */
    @Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentasPorCliente(long dni) {
        return cuentaRepository.findByTitularDni(dni);
    }

    /**
     * Obtiene las cuentas de un cliente en una moneda específica
     *
     * @param dni DNI del cliente
     * @param moneda Moneda
     * @return Lista de cuentas en esa moneda
     */
    @Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentasPorClienteYMoneda(long dni, TipoMoneda moneda) {
        return cuentaRepository.findByTitularDniAndMoneda(dni, moneda);
    }

    /**
     * Valida que el tipo de cuenta esté soportado por el banco
     * Cuentas soportadas:
     * - CA$ (Caja de Ahorro en Pesos)
     * - CC$ (Cuenta Corriente en Pesos)
     * - CAU$S (Caja de Ahorro en Dólares)
     * NO soportada:
     * - CCU$S (Cuenta Corriente en Dólares)
     *
     * @param tipoCuenta Tipo de cuenta
     * @param moneda Moneda
     * @throws IllegalArgumentException Si el tipo no está soportado
     */
    private void validarTipoCuentaSoportada(TipoCuenta tipoCuenta, TipoMoneda moneda) {
        // Cuenta Corriente en Dólares no está soportada
        if (tipoCuenta == TipoCuenta.CUENTA_CORRIENTE && moneda == TipoMoneda.DOLARES) {
            throw new IllegalArgumentException("El banco no soporta Cuentas Corrientes en Dólares");
        }
    }

    /**
     * Asocia un titular a una cuenta y verifica que no tenga ya una cuenta del mismo tipo y moneda
     *
     * @param cuenta Cuenta a asociar
     * @param dniTitular DNI del titular
     * @throws TipoCuentaAlreadyExistsException Si el cliente ya tiene una cuenta del mismo tipo y moneda
     */
    public void asociarTitular(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException {
        Cliente titular = clienteService.buscarClientePorDni(dniTitular);
        cuenta.setTitular(titular);

        // Verificar si ya tiene una cuenta del mismo tipo y moneda
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda");
        }

        titular.addCuenta(cuenta);
    }
}