package cosn.group2.realsystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@OpenAPIDefinition(servers = {@Server(
		url = "https://cosn-api-gateway.herokuapp.com", description = "Default Server URL")})
@SpringBootApplication
public class RealSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealSystemApplication.class, args);
	}

	// for outer micro-services
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setReadTimeout(Duration.ofSeconds(5))
				.build();
	}
}
