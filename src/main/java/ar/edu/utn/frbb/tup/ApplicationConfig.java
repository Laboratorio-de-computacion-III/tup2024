package ar.edu.utn.frbb.tup;


import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@ComponentScan
public class ApplicationConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Bancario TUP 2024")
                        .description("API REST para la gestión de un sistema bancario completo. " +
                                "Incluye gestión de clientes, cuentas, transferencias y préstamos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Joaquin Auday")
                                .email("joaquin.auday@gmail.com")));
    }
}
