package cosn.group2.realsystem.kafka;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cosn.group2.realsystem.dbg.KafkaConsumer;
import cosn.group2.realsystem.model.Log;
import cosn.group2.realsystem.model.SensorData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@DirtiesContext
// @EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaIntegrationTest {
    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @BeforeEach
    void setUp() {
        consumer.reset();
    }

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenLogReceived() throws Exception {
        Log log = Log.instance(Log.LogType.WARNING, Log.OperationType.OTHER, "Sending with own simple KafkaProducer");
        producer.log(log);
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        System.out.println(consumer.getLog());
        System.out.println(log);
        assertThat(consumer.getLog(), equalTo(log));
    }

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMessageReceived() throws Exception {
        String message = "Sending with own simple KafkaProducer";
        producer.sendMessage(message);
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        assertThat(consumer.getMessage(), containsString(message));
    }

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenSensorDataReceived() throws Exception {

        SensorData sensorData = new SensorData();
        sensorData.value = String.valueOf(Math.random());
        sensorData.unit = "G-Unit";
        sensorData.timestamp = String.valueOf(System.currentTimeMillis());

        producer.sendSensorData(sensorData);
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        assertThat(consumer.getSensorData(), equalTo(sensorData));
    }
}