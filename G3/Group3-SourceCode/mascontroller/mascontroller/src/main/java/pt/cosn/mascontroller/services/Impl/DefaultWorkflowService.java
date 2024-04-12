package pt.cosn.mascontroller.services.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.cosn.mascontroller.dtos.WorkflowDto;
import pt.cosn.mascontroller.services.WorkflowService;

@Service
public class DefaultWorkflowService implements WorkflowService {

  private static final Logger log = LoggerFactory.getLogger(DefaultWorkflowService.class);

  private final RestTemplate restTemplate;

  @Value("${service.workflow.api}")
  private String END_POINT;

  public DefaultWorkflowService(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
  }

  @Override
  public WorkflowDto findById(String id) {
    try{
      return restTemplate.getForObject(END_POINT + id, WorkflowDto.class);
    } catch (Exception exception){
      log.error("findById error: ", exception);
    }
    return null;
  }
}
