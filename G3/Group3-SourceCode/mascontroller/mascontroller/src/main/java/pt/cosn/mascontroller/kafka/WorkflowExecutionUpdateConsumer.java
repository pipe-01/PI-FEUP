package pt.cosn.mascontroller.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pt.cosn.mascontroller.dtos.AlgorithmDto;
import pt.cosn.mascontroller.dtos.WorkflowDto;
import pt.cosn.mascontroller.dtos.requests.ArtificialSystemRequestDto;
import pt.cosn.mascontroller.dtos.requests.MonitoringLogMessageDto;
import pt.cosn.mascontroller.dtos.responses.ArtificialSystemResponseDto;
import pt.cosn.mascontroller.models.WorkflowExecution;
import pt.cosn.mascontroller.services.Impl.DefaultDomainModelService;
import pt.cosn.mascontroller.services.Impl.DefaultWorkflowExecutionService;

@Component
public class WorkflowExecutionUpdateConsumer {

  private static final Logger log = LoggerFactory.getLogger(WorkflowExecutionUpdateConsumer.class);

  private final DefaultWorkflowExecutionService workflowExecutionService;

  public WorkflowExecutionUpdateConsumer(
      DefaultWorkflowExecutionService workflowExecutionService) {
    this.workflowExecutionService = workflowExecutionService;
  }


  @KafkaListener(topics = "workflow_execution_result")
  public void listenWorkflowExecution(String message) {
    try {
      System.out.println("Received Message: " + message);
      ArtificialSystemResponseDto responseDto = new ObjectMapper().readValue(message, ArtificialSystemResponseDto.class);

      Optional<WorkflowExecution> workflowExecutionOptional = workflowExecutionService.findWorkflowExecutionById(responseDto.getWorkflowExecutionId());
      workflowExecutionOptional.ifPresentOrElse( workflowExecution -> {
        WorkflowDto workflowDto = workflowExecutionService.getWorkflowService().findById(workflowExecution.getWorkflowId());
        if(workflowExecution.getCurrentStep() < workflowExecution.getSteps()){
          AlgorithmDto algorithmDto = workflowDto.getAlgorithms().get(workflowExecution.getCurrentStep());

          ArtificialSystemRequestDto requestDto = ArtificialSystemRequestDto.builder()
              .workflowExecutionId(workflowExecution.getId())
              .algorithm(algorithmDto)
              .path(responseDto.getPath())
              .build();
          if(workflowExecutionService.getArtificialSystemService().execute(requestDto)){
            workflowExecution.setCurrentStep(workflowExecution.getCurrentStep() + 1);
            workflowExecutionService.getWorkflowExecutionRepository().save(workflowExecution);

            workflowExecutionService.registerExecutionLog(workflowExecution, algorithmDto.getName(), requestDto.getPath());
          }
        }
      }, () -> {
        workflowExecutionService.getMonitoringService().registerEvent(MonitoringLogMessageDto
            .builder()
            .serviceName("MAS Controller")
            .messageType("WARNING")
            .operationType("READ")
            .message("The following message from the Artificial System was ignored because it refers to an Workflow execution which does not exists anymore: " + message)
            .build());
      });
    } catch (JsonProcessingException e) {
      log.error("listenWorkflowExecution error: ", e);
    }

  }
}
