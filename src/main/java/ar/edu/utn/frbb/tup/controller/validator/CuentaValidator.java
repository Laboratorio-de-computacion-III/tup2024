package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;

@Component
public class CuentaValidator {

    /**
     * Valida los datos básicos de la cuenta
     *
     * @param cuentaDto Datos de la cuenta a validar
     */
    public void validate(CuentaDto cuentaDto) {
        if (cuentaDto.getNumeroCliente() <= 0) {
            throw new IllegalArgumentException("Número de cliente inválido");
        }

        if (cuentaDto.getTipoCuenta() == null) {
            throw new IllegalArgumentException("El tipo de cuenta es requerido");
        }

        if (cuentaDto.getMoneda() == null) {
            throw new IllegalArgumentException("La moneda es requerida");
        }
    }
}
