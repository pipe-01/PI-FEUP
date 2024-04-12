package cosn.group2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cosn.group2.Model.Log;
import cosn.group2.dto.MASResponse;
import cosn.group2.kafka.KafkaConsumer;
import cosn.group2.kafka.KafkaProducer;
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
        Log log = Log.instance(Log.LogType.WARNING, Log.OperationType.OTHER, "Sending with own KafkaProducer");
        producer.log(log);
        consumer.getLatch().await(20000, TimeUnit.MILLISECONDS);

        System.out.println(consumer.getLog());
        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        System.out.println(consumer.getLog());
        System.out.println(log);
        assertThat(consumer.getLog(), equalTo(log.toString()));
    }

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMASMessageReceived() throws Exception {

        String masResponse = "{\"workflowExecutionId\": 1 ,\"path\": \"1- step1.txt\"}";
                //new MASResponse(1, "1- step1.txt");

        producer.sendTask(masResponse);
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        assertThat(consumer.getMessage(), equalTo(masResponse));
    }
}