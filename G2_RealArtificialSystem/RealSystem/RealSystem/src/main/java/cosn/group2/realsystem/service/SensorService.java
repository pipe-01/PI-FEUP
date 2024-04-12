package cosn.group2.realsystem.service;

import cosn.group2.realsystem.model.Sensor;
import cosn.group2.realsystem.repository.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Service
public class SensorService {

    private static final Logger LOG = LoggerFactory.getLogger(SensorService.class);

    // of city microservice
    @Value("${service.city}" )
    public String BASE_URL;

    @Autowired
    private SensorRepository repository;

    private final CircuitBreaker circuitBreaker;

    @Autowired
    private RestTemplate restTemplate;

    public SensorService(Resilience4JCircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
    }

    public Sensor getSensor(String tokenBearer, String sensorID) throws Exception {
        LOG.debug("requesting sensor to city service " + sensorID);

        //
        // find in db first
        Sensor staticSensor = repository.findStaticSensorByID(sensorID);
        Sensor mobileSensor = repository.findMobileSensorByID(sensorID);

        if (staticSensor != null) return staticSensor;
        if (mobileSensor != null) return mobileSensor;

        //
        //

        final String token = tokenBearer.replace("Bearer ", "");

        final String BASE_URL_STATIC = BASE_URL + "/sensor/static";
        final String BASE_URL_MOBILE = BASE_URL + "/sensor/mobile";
        final String port = environment.getProperty("local.server.port");
        final String baseUrlStatic = BASE_URL_STATIC.replace("localhost", "localhost:" + port);
        final String baseUrlMobile = BASE_URL_MOBILE.replace("localhost", "localhost:" + port);
        final String urlStatic = baseUrlStatic + "/" + sensorID;
        final String urlMobile = baseUrlMobile + "/" + sensorID;


        final Supplier<Sensor> sensorSupplier = () -> {

            Sensor sensor = null;

            final HttpHeaders headers = new HttpHeaders();
            headers.set(ServiceConstants.AUTHORIZATION_TOKEN, token);

            try {
                sensor = restTemplate
                        .exchange(urlStatic, HttpMethod.GET, new HttpEntity<>(headers), Sensor.class).getBody();
            } catch (HttpClientErrorException.NotFound e) {
                e.printStackTrace();
            }
            try {
                if (sensor == null) {
                    sensor = restTemplate
                            .exchange(urlMobile, HttpMethod.GET, new HttpEntity<>(headers), Sensor.class).getBody();
                }
            } catch (HttpClientErrorException.NotFound e) {
                e.printStackTrace();
            }

            if (sensor == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "sensor not found");

            // safe in repository
            repository.save(sensor);

            return sensor;
        };

        if (!ENABLE_CIRCUIT_BREAKER) sensorSupplier.get();
        return circuitBreaker.run(sensorSupplier, throwable -> {
            LOG.warn("Error making request to city service", throwable);
            throw new RuntimeException(throwable);
        });
    }

    @Autowired
    Environment environment;

    @Value("${dev.ENABLE_CIRCUIT_BREAKER}" )
    public boolean ENABLE_CIRCUIT_BREAKER;
}
