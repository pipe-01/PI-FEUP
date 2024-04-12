package pt.cosn.mascontroller.services.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.cosn.mascontroller.dtos.AlgorithmDto;
import pt.cosn.mascontroller.dtos.DomainModelDto;
import pt.cosn.mascontroller.dtos.WorkflowDto;
import pt.cosn.mascontroller.dtos.requests.ArtificialSystemRequestDto;
import pt.cosn.mascontroller.dtos.requests.MonitoringLogMessageDto;
import pt.cosn.mascontroller.services.ArtificialSystemService;

@Service
public class DefaultArtificialSystemService implements ArtificialSystemService {

  private static final Logger log = LoggerFactory.getLogger(DefaultArtificialSystemService.class);

  private final RestTemplate restTemplate;

  @Autowired
  private DefaultMonitoringService monitoringService;

  @Value("${service.artificial-system.api}")
  private String END_POINT;

  public DefaultArtificialSystemService(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
  }

  @Override
  public boolean execute(ArtificialSystemRequestDto requestDto) {
    try{
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<ArtificialSystemRequestDto> request = new HttpEntity<>(requestDto, headers);

      ResponseEntity<String> response = restTemplate.postForEntity(END_POINT, request , String.class);
      return HttpStatus.OK.equals(response.getStatusCode());
    } catch (Exception exception){
      log.error("execute error: ", exception);
      monitoringService.registerEvent(MonitoringLogMessageDto
          .builder()
          .serviceName("MAS Controller")
          .messageType("DEBUG")
          .operationType("UPDATE")
          .message("Error trying to send workflow execution step to the Artificial System: "
          + " WorkflowExecutionId: " + requestDto.getWorkflowExecutionId()
          + " Algorithm: " + requestDto.getAlgorithm()
          + " Path: " + requestDto.getPath())
          .build());
    }
    return false;
  }

}
