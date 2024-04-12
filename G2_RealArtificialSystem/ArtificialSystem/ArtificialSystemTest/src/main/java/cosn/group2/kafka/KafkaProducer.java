package cosn.group2.kafka;

import cosn.group2.Model.Log;
import cosn.group2.Model.Task;
import cosn.group2.dto.MASResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static cosn.group2.kafka.ApplicationConstant.*;

@Component
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public String sendTask(String workflowResponse) {

		try {
			kafkaTemplate.send(TOPIC_NAME_EXPERIENCE, workflowResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json message sent successfully";
	}


	/*public void log(String message) {
		log(Log.instance(message));
	}
*/
	public String log(Log log) {

		log.serviceName = "ArtificialSystem";

		try {
			kafkaTemplate.send(TOPIC_NAME_LOGGING, log.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "json message sent successfully";
	}
}
