package ar.edu.utn.frbb.tup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.edu.utn.frbb.tup.controller.PrestamoController;

@Service
public class CalificacionCrediticiaService {
    private static final Logger log = LoggerFactory.getLogger(PrestamoController.class);

    private static final String EXTERNAL_API_URL =
            "https://www.random.org/integers/?num=1&min=1&max=100&col=1&base=10&format=plain&rnd=new&dni=%d";

    /**
     * Llamada a una API externa para evaluar la calificación crediticia de un cliente.
     *
     * @param dni DNI del cliente a evaluar
     * @return true si la calificación es aprobada, false en caso contrario
     */
    public boolean verificarCalificacionCrediticia(long dni) {
        RestTemplate restTemplate = new RestTemplate();

        try {

            String urlWithDni = String.format(EXTERNAL_API_URL, dni);
            String response = restTemplate.getForObject(urlWithDni, String.class);
            assert response != null;
            int randomNumber = Integer.parseInt(response.trim());
            log.info("Respuesta de servicio evaluacion crediticia para DNI {}: {}", dni,
                    randomNumber % 2 == 0 ? "Aprobado" : "Rechazado");

            return randomNumber % 2 == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}