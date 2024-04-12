package cosn.group2.realsystem.controller;

import cosn.group2.realsystem.kafka.KafkaProducer;
import cosn.group2.realsystem.model.Log;
import cosn.group2.realsystem.model.Log.LogType;
import cosn.group2.realsystem.model.Log.OperationType;
import cosn.group2.realsystem.model.Sensor;
import cosn.group2.realsystem.model.SensorData;
import cosn.group2.realsystem.repository.RealSystemRepository;
import cosn.group2.realsystem.service.SensorService;
import cosn.group2.realsystem.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static cosn.group2.realsystem.service.ServiceConstants.*;

@SuppressWarnings({"unused", "OptionalUsedAsFieldOrParameterType"})
@RestController
@Tag(name = "RealSystem", description = "API for Administrator Information")
public class RealSystemController {

    private static final Logger LOG = LoggerFactory.getLogger(RealSystemController.class);

    private final RealSystemRepository repository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private VerificationService verificationService;

    public RealSystemController(RealSystemRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/health")
    public String health() {
        return "Up and running!";
    }

    @GetMapping("")
    List<SensorData> all(@RequestHeader(AUTHORIZATION_TOKEN) Optional<String> token) {
        kafkaProducer.log(Log.instance(LogType.DEBUG, OperationType.READ, "GET: /sensors-data"));

        verification(token);

        return repository.findAll();
    }

    @PostMapping("")
    SensorData newSensorData(@RequestBody SensorData newSensorData,
                             @RequestHeader(AUTHORIZATION_TOKEN) Optional<String> token) {

        kafkaProducer.log(Log.instance(LogType.DEBUG, OperationType.CREATE, "POST: /sensor-data"));

        verification(token);

        verificationSensor(newSensorData.sensorID, token);

        final String sensorID = newSensorData.sensorID;

        return repository.findById(sensorID)
                .filter(sensorData -> Objects.equals(sensorData.sensorID, newSensorData.sensorID))
                .map(sensorData -> {
                    sensorData.unit = newSensorData.unit;
                    sensorData.timestamp = newSensorData.timestamp;
                    sensorData.value = newSensorData.value;
                    return repository.save(sensorData);
                })
                .orElseGet(() -> {
                    newSensorData.sensorID = sensorID;
                    return repository.save(newSensorData);
                });
    }

    // Single item
    @Operation(summary = "Retrieve Sensor Data by ID", description = "Retrieve Sensor Data by ID", tags = {"sensorData"})
    @ApiResponses({@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SensorData.class))))})
    @GetMapping("/{id}")
    public List<SensorData> one(@PathVariable String id,
                                @RequestHeader(AUTHORIZATION_TOKEN) Optional<String> token,
                                @RequestHeader(START_TIMESTAMP) Optional<String> fromTimestamp,
                                @RequestHeader(END_TIMESTAMP) Optional<String> toTimestamp) {

        kafkaProducer.log(Log.instance(LogType.DEBUG, OperationType.READ, "GET: /sensors-data/" + id));

        verification(token);

        verificationSensor(id, token);

        List<SensorData> sensorDatas = repository.findBySensorID(id);

        if (sensorDatas == null) throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "sensor data not found for " + id);

        final Long[] from = new Long[]{null};
        final Long[] end = new Long[]{null};
        try { from[0] = fromTimestamp.isEmpty() ? null : Long.parseLong(fromTimestamp.get()); } catch (Exception e) {e.printStackTrace();}
        try { end[0] = toTimestamp.isEmpty() ? null : Long.parseLong(toTimestamp.get()); } catch (Exception e) {e.printStackTrace();}

        return sensorDatas.stream()
                .filter(sensorData -> {
                    if (sensorData.timestamp == null) return true;
                    if (from[0] == null) return true;

                    try {
                        final long timestamp = Long.parseLong(sensorData.timestamp);
                        return timestamp >= from[0];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .filter(sensorData -> {
                    if (sensorData.timestamp == null) return true;
                    if (end[0] == null) return true;

                    try {
                        final long timestamp = Long.parseLong(sensorData.timestamp);
                        return timestamp <= end[0];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private void verification(Optional<String> token) {
        verification(token.orElse(null));
    }

    private void verification(String token) {

        try {
            if (!verificationService.isUser(token))
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }

    private void verificationSensor(String id, Optional<String> token) {
        verificationSensor(id, token.orElse(null));
    }

    private void verificationSensor(String id, String token) {

        try {
            final Sensor sensor = sensorService.getSensor(token, id);
        } catch (Exception e) {
            LOG.warn("Error making request to city service", e);
            if (MUST_FAIL_IF_UNAVAILABLE)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not verify that sensor exists");
        }
    }

    @Value("${dev.CITY_SERVICE_MUST_FAIL_IF_UNAVAILABLE}" )
    public boolean MUST_FAIL_IF_UNAVAILABLE;
}
