package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.CuentaService;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de cuentas
 */
@RestController
@RequestMapping("/cuenta")
@Tag(name = "Cuentas", description = "Operaciones relacionadas con la gestión de cuentas del banco")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto)
            throws TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException {

        cuentaValidator.validate(cuentaDto);

        Cliente cliente = clienteService.buscarClientePorDni(cuentaDto.getNumeroCliente());
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
        cuenta.setMoneda(cuentaDto.getMoneda()); // LÍNEA AGREGADA
        cuentaService.darDeAltaCuenta(cuenta, cuentaDto.getNumeroCliente());
        cuenta.setTitular(cliente);

        return cuenta;
    }
}
