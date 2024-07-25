package ar.edu.utn.frbb.tup.controller.validator;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;


@Component
public class CuentaValidator {

    public void validate(CuentaDto cuentaDto) {
        validateTipoCuenta(cuentaDto);
        validateMoneda(cuentaDto);
    }

    private void validateTipoCuenta(CuentaDto cuentaDto) {
        if (!"C".equals(cuentaDto.getTipoCuenta()) || !"A".equals(cuentaDto.getTipoCuenta())) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto");
        }
    }

    private void validateMoneda(CuentaDto cuentaDto) {
        if (!"P".equals(cuentaDto.getMoneda()) || !"D".equals(cuentaDto.getMoneda())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
    }
}
