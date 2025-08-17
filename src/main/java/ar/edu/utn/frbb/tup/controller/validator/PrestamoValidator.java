package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.PrestamoDto;

@Component
public class PrestamoValidator {

    public void validate(PrestamoDto prestamoDto) {
        // Validar monto del préstamo
        if (prestamoDto.getMontoPrestamo() <= 0) {
            throw new IllegalArgumentException("El monto del préstamo debe ser mayor a 0");
        }

        // Validar plazo en meses
        if (prestamoDto.getPlazoMeses() <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor a 0 meses");
        }

        if (prestamoDto.getPlazoMeses() > 60) {
            throw new IllegalArgumentException("El plazo máximo es de 60 meses");
        }

        // Validar moneda
        if (prestamoDto.getMoneda() == null || prestamoDto.getMoneda().isEmpty()) {
            throw new IllegalArgumentException("La moneda es requerida");
        }

        String moneda = prestamoDto.getMoneda().toUpperCase();
        if (!moneda.equals("PESOS") && !moneda.equals("DOLARES")) {
            throw new IllegalArgumentException("Moneda inválida. Use PESOS o DOLARES");
        }
    }
}
