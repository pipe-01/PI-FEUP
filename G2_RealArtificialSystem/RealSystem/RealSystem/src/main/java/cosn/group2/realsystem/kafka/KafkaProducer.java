package cosn.group2.realsystem.kafka;

import cosn.group2.realsystem.model.Log;
import cosn.group2.realsystem.model.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static cosn.group2.realsystem.kafka.ApplicationConstant.*;

@Component
public class KafkaProducer {

	@Value("${dev.KAFKA_LOGGING_ENABLED}" )
	private boolean KAFKA_LOGGING_ENABLED;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void sendSensorData(SensorData sensorData) {

		try {
			kafkaTemplate.send(TOPIC_NAME_SENSOR_DATA, sensorData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {

		try {
			kafkaTemplate.send(TOPIC_NAME_MESSAGE, message.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void log(String message) {
		log(Log.instance(message));
	}

	public void log(Log log) {

		if (!KAFKA_LOGGING_ENABLED) return;

		log.serviceName = "RealSystem";

		try {
			kafkaTemplate.send(TOPIC_NAME_LOGGING, log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
