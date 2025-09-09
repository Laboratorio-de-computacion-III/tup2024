package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;

@Component
public class CuentaValidator {

    /**
     * Valida los datos básicos de la cuenta
     *
     * @param cuentaDto Datos de la cuenta a validar
     */
    public void validate(CuentaDto cuentaDto) {
        // Validar número de cliente
        if (cuentaDto.getNumeroCliente() <= 0) {
            throw new IllegalArgumentException("Número de cliente inválido");
        }

        // Validar tipo de cuenta
        if (cuentaDto.getTipoCuenta() == null) {
            throw new IllegalArgumentException("El tipo de cuenta es requerido");
        }

        // Validar moneda
        if (cuentaDto.getMoneda() == null) {
            throw new IllegalArgumentException("La moneda es requerida");
        }
    }

    /**
     * Valida que el tipo de cuenta esté soportado por el banco.
     * Cuentas soportadas:
     * - CA$ (Caja de Ahorro en Pesos)
     * - CC$ (Cuenta Corriente en Pesos)
     * - CAU$S (Caja de Ahorro en Dólares)
     *
     * NO soportada:
     * - CCU$S (Cuenta Corriente en Dólares)
     *
     * @param tipoCuenta Tipo de cuenta
     * @param moneda Moneda de la cuenta
     */
    public void validateTipoCuentaSoportada(TipoCuenta tipoCuenta, TipoMoneda moneda) {
        // Cuenta Corriente en Dólares no está soportada
        if (tipoCuenta == TipoCuenta.CUENTA_CORRIENTE && moneda == TipoMoneda.DOLARES) {
            throw new IllegalArgumentException("El banco no soporta Cuentas Corrientes en Dólares");
        }
    }
}
