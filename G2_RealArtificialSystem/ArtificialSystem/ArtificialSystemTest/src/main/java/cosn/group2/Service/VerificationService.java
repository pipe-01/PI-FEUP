package cosn.group2.Service;

import cosn.group2.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class VerificationService {

    private static final Logger LOG = LoggerFactory.getLogger(VerificationService.class);

    public static final String AUTHORIZATION_TOKEN = "Authorization";

    @Value("${dev.VERIFY_TOKEN}" )
    private boolean VERIFY_TOKEN;

    // of gateway microservice
    @Value("${service.gateway}" )
    private String BASE_URL;

    private final CircuitBreaker circuitBreaker;

    @Autowired
    private RestTemplate restTemplate;

    public VerificationService(Resilience4JCircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
    }

    public boolean isUser(String tokenBearer) throws Exception {
        LOG.debug("requesting user to getaway ");

        if (tokenBearer == null) throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);

        if (!VERIFY_TOKEN) return true;

        final String token = tokenBearer.replace("Bearer ", "");

        final String port = environment.getProperty("local.server.port");
        final String baseUrl = BASE_URL.replace("localhost", "localhost:" + port);
        final String url = baseUrl + "/getUserFromToken";

        final Supplier<Boolean> isUserSupplier = () -> {

            final Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", token);

            try {

                ResponseEntity<User> response = restTemplate
                        .exchange(url, HttpMethod.POST, new HttpEntity<>(tokenMap), User.class);

                // if found
                return response.getStatusCode().equals(HttpStatus.OK)
                        // and if user is Researcher
                        && "Researcher".equals(response.getBody().role);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        };

        return circuitBreaker.run(isUserSupplier, throwable -> {
            LOG.warn("Error making request to city service", throwable);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        });
    }

    @Autowired
    Environment environment;
}
