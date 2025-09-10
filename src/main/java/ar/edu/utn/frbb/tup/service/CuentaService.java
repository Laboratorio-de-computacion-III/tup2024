package ar.edu.utn.frbb.tup.service;

import java.util.List;
import java.util.Optional;
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

        // Verificar si la cuenta ya existe por número
        if (cuentaRepository.existsById(cuenta.getNumeroCuenta())) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        // Verificar si el cliente ya tiene una cuenta del mismo tipo y moneda
        if (cuentaRepository.existsByTitularDniAndTipoCuentaAndMoneda(
                dniTitular, cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException(
                    "El cliente ya posee una cuenta " + cuenta.getTipoCuenta() + " en " + cuenta.getMoneda());
        }

        // Validar que el tipo de cuenta esté soportado
        validarTipoCuentaSoportada(cuenta.getTipoCuenta(), cuenta.getMoneda());
        this.asociarTitular(cuenta, dniTitular);
        // Guardar la cuenta
        cuentaRepository.save(cuenta);

        // Agregar la cuenta al cliente
        clienteService.asociarCuenta(cuenta, dniTitular);

        System.out.println("Cuenta creada exitosamente: " + cuenta);
        log.info("Cuenta creada exitosamente: {}", cuenta);
    }

    /**
     * Busca una cuenta por número
     *
     * @param numeroCuenta Número de cuenta
     * @return Cuenta encontrada o null
     */
    @Transactional(readOnly = true)
    public Cuenta find(long numeroCuenta) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(numeroCuenta);
        return cuenta.orElse(null);
    }

    /**
     * Actualiza una cuenta existente
     *
     * @param cuenta Cuenta a actualizar
     * @return Cuenta actualizada
     */
    public Cuenta actualizarCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
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
     * Busca una cuenta específica por DNI, tipo y moneda
     *
     * @param dni DNI del titular
     * @param tipoCuenta Tipo de cuenta
     * @param moneda Moneda
     * @return Cuenta encontrada o null
     */
    @Transactional(readOnly = true)
    public Cuenta buscarCuentaPorTipoYMoneda(long dni, TipoCuenta tipoCuenta, TipoMoneda moneda) {
        Optional<Cuenta> cuenta = cuentaRepository.findByTitularDniAndTipoCuentaAndMoneda(dni, tipoCuenta, moneda);
        return cuenta.orElse(null);
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
     * Calcula el balance total de un cliente en una moneda
     *
     * @param dni DNI del cliente
     * @param moneda Moneda
     * @return Balance total en centavos
     */
    @Transactional(readOnly = true)
    public long calcularBalanceTotalPorMoneda(long dni, TipoMoneda moneda) {
        return cuentaRepository.getTotalBalanceByDniAndMoneda(dni, moneda);
    }

    /**
     * Valida que el tipo de cuenta esté soportado por el banco
     * Cuentas soportadas:
     * - CA$ (Caja de Ahorro en Pesos)
     * - CC$ (Cuenta Corriente en Pesos)
     * - CAU$S (Caja de Ahorro en Dólares)
     *
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
     * Elimina una cuenta por número
     *
     * @param numeroCuenta Número de cuenta a eliminar
     * @throws IllegalArgumentException Si la cuenta no existe
     */
    public void eliminarCuenta(long numeroCuenta) {
        if (!cuentaRepository.existsById(numeroCuenta)) {
            throw new IllegalArgumentException("La cuenta no existe");
        }
        cuentaRepository.deleteById(numeroCuenta);
    }

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