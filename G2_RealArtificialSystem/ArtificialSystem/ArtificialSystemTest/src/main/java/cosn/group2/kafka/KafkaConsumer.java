package cosn.group2.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cosn.group2.Model.Log;
import cosn.group2.Model.Task;
import cosn.group2.dto.MASResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

import static cosn.group2.kafka.ApplicationConstant.GROUP_ID_JSON;
import static cosn.group2.kafka.ApplicationConstant.TOPIC_NAME_EXPERIENCE;

/**
 * DEBUG
 */
@Component
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@KafkaListener(groupId = GROUP_ID_JSON, topics = TOPIC_NAME_EXPERIENCE,
			containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public void receivedExperienceMessage(String message) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(message);
		logger.info("Json message received using Kafka listener " + jsonString);

		this.message = message;
		this.latch.countDown();
	}

	@KafkaListener(groupId = GROUP_ID_JSON, topics = ApplicationConstant.TOPIC_NAME_LOGGING, containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public void receivedLogMessage(String log) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(log);
		logger.info("Json message received using Kafka listener " + log);

		this.log = log;
		this.latch.countDown();
	}

	private CountDownLatch latch = new CountDownLatch(1);
	private String message;
	private String log;

	public void reset() {
		this.latch = new CountDownLatch(1);
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public String getMessage() {
		return message;
	}

	public String getLog() {
		return log;
	}
}
