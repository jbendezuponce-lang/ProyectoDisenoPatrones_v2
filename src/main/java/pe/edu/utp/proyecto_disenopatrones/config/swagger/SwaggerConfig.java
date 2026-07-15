package pe.edu.utp.proyecto_disenopatrones.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
/**
 * http://localhost:8090/api/swagger-ui/index.html
 * https://prezi.com/view/r4DP4TCmYUJk1eaqjKG4/
 */
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().
                        title("Academico API")
                        .description("Sistema Academico")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
