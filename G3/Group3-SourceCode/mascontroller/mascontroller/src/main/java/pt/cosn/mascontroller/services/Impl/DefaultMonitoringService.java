package pt.cosn.mascontroller.services.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pt.cosn.mascontroller.dtos.requests.MonitoringLogMessageDto;
import pt.cosn.mascontroller.services.MonitoringService;

@Service
public class DefaultMonitoringService implements MonitoringService {

  private static final Logger log = LoggerFactory.getLogger(DefaultDomainModelService.class);

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void registerEvent(MonitoringLogMessageDto monitoringLogMessageDto) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      kafkaTemplate.send("cosn_logging", mapper.writeValueAsString(monitoringLogMessageDto));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (Exception exception){
      log.error("registerEvent error ", exception);
    }
  }
}
