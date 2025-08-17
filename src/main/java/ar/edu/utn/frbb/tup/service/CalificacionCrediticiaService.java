package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Service;

@Service
public class CalificacionCrediticiaService {

    /**
     * Simula un servicio externo de calificación crediticia.
     * En un escenario real, esto haría una llamada HTTP a un servicio externo.
     * Por simplicidad, retorna true el 80% de las veces (simulando buena calificación).
     *
     * @param dni DNI del cliente a evaluar
     * @return true si el cliente tiene buena calificación crediticia
     */
    public boolean verificarCalificacionCrediticia(long dni) {
        // Simulación de llamada a servicio externo
        // En producción esto sería una llamada REST real
        try {
            Thread.sleep(500); // Simula latencia de red
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Para fines de testing, usar el DNI para determinar el resultado
        // DNIs que terminan en 0 o 1 tienen mala calificación (20% de rechazo)
        return dni % 10 >= 2;
    }
}
