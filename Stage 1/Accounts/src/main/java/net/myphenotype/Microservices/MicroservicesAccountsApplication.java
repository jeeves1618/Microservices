package net.myphenotype.Microservices;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditorAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservice REST API Documentation",
				description = "Learning microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Right Ho Jeeves",
						email = "jeeves@pgwodehousefan.com",
						url = "https://jeeves1618.github.io/Learnings/"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://jeeves1618.github.io/Learnings/"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "Learning microservice REST API Documentation",
				url = "http://localhost:8080/swagger-ui/index.html"
		)
)
public class MicroservicesAccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesAccountsApplication.class, args);
	}

}
