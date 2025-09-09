package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.dto.ConsultaPrestamoResponseDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoResponseDto;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.model.exception.CreditoRechazadoException;
import ar.edu.utn.frbb.tup.model.exception.PrestamoException;
import ar.edu.utn.frbb.tup.service.PrestamoService;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de prestamos
 */
@RestController
@RequestMapping("/api/prestamo")
@Tag(name = "Prestamos", description = "Operaciones relacionadas con la gestión de prestamos del banco")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private PrestamoValidator prestamoValidator;

    @PostMapping
    public ResponseEntity<PrestamoResponseDto> solicitarPrestamo(@RequestBody PrestamoDto prestamoDto) {
        PrestamoResponseDto response = new PrestamoResponseDto();

        try {
            // Log para debugging
            System.out.println("Solicitud de préstamo recibida: DNI=" + prestamoDto.getNumeroCliente() +
                    ", Monto=" + prestamoDto.getMontoPrestamo() +
                    ", Plazo=" + prestamoDto.getPlazoMeses() +
                    ", Moneda=" + prestamoDto.getMoneda());

            // Validar datos de entrada
            prestamoValidator.validate(prestamoDto);

            // Procesar solicitud de préstamo
            response = prestamoService.solicitarPrestamo(prestamoDto);

            return ResponseEntity.ok(response);

        } catch (CreditoRechazadoException e) {
            System.err.println("Crédito rechazado: " + e.getMessage());
            response.setEstado("RECHAZADO");
            response.setMensaje(e.getMessage());
            return ResponseEntity.ok(response);

        } catch (PrestamoException e) {
            System.err.println("Error de préstamo: " + e.getMessage());
            response.setEstado("RECHAZADO");
            response.setMensaje(e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (IllegalArgumentException e) {
            System.err.println("Argumentos inválidos: " + e.getMessage());
            response.setEstado("RECHAZADO");
            response.setMensaje(e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            response.setEstado("ERROR");
            response.setMensaje("Error procesando la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<ConsultaPrestamoResponseDto> consultarPrestamos(@PathVariable long clienteId) {
        try {
            ConsultaPrestamoResponseDto response = prestamoService.consultarPrestamos(clienteId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
