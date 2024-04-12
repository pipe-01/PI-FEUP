package pt.cosn.mascontroller.services.Impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Data;
import org.springframework.stereotype.Service;
import pt.cosn.mascontroller.dtos.AlgorithmDto;
import pt.cosn.mascontroller.dtos.DomainModelDto;
import pt.cosn.mascontroller.dtos.LogDto;
import pt.cosn.mascontroller.dtos.WorkflowDto;
import pt.cosn.mascontroller.dtos.requests.ArtificialSystemRequestDto;
import pt.cosn.mascontroller.dtos.requests.MonitoringLogMessageDto;
import pt.cosn.mascontroller.dtos.requests.WorkflowExecutionRequestDto;
import pt.cosn.mascontroller.dtos.responses.WorkflowExecutionListResponseDto;
import pt.cosn.mascontroller.dtos.responses.WorkflowExecutionResponseDto;
import pt.cosn.mascontroller.models.WorkflowExecution;
import pt.cosn.mascontroller.models.Log;
import pt.cosn.mascontroller.repositories.WorkflowExecutionRepository;
import pt.cosn.mascontroller.repositories.LogRepository;
import pt.cosn.mascontroller.services.AlgorithmService;
import pt.cosn.mascontroller.services.ArtificialSystemService;
import pt.cosn.mascontroller.services.DomainModelService;
import pt.cosn.mascontroller.services.MonitoringService;
import pt.cosn.mascontroller.services.WorkflowExecutionService;
import pt.cosn.mascontroller.services.WorkflowService;

@Data
@Service
public class DefaultWorkflowExecutionService implements WorkflowExecutionService {

  private final WorkflowExecutionRepository workflowExecutionRepository;

  private final LogRepository logRepository;

  private final WorkflowService workflowService;

  private final AlgorithmService algorithmService;

  private final DomainModelService domainModelService;

  private final ArtificialSystemService artificialSystemService;

  private final MonitoringService monitoringService;

  public DefaultWorkflowExecutionService(
      WorkflowExecutionRepository workflowExecutionRepository,
      LogRepository logRepository, WorkflowService workflowService,
      AlgorithmService algorithmService,
      DomainModelService domainModelService,
      ArtificialSystemService artificialSystemService,
      MonitoringService monitoringService) {
    this.workflowExecutionRepository = workflowExecutionRepository;
    this.logRepository = logRepository;
    this.workflowService = workflowService;
    this.algorithmService = algorithmService;
    this.domainModelService = domainModelService;
    this.artificialSystemService = artificialSystemService;
    this.monitoringService = monitoringService;
  }

  @Override
  public WorkflowExecutionResponseDto findById(Long id) {
    WorkflowExecutionResponseDto response = WorkflowExecutionResponseDto.builder().build();
    Optional<WorkflowExecution> optionalWorkflowExecution = workflowExecutionRepository.findById(id);
    optionalWorkflowExecution.ifPresentOrElse(workflowExecution -> {
      response.setGoodState("Workflow found!");
      response.setWorkflowExecution(workflowExecution);
      response.setLogs(getLogs(workflowExecution));
    }, () -> {
      response.setBadState(
          "Workflow: " + id + " not found. Please make sure you have the correct id and try again.");
    });
    return response;
  }

  @Override
  public Optional<WorkflowExecution> findWorkflowExecutionById(Long id) {
    return workflowExecutionRepository.findById(id);
  }


  @Override
  public WorkflowExecutionListResponseDto findByUserId(String userId) {
    Iterable<WorkflowExecution> iterable = workflowExecutionRepository.findByUserId(userId);

    WorkflowExecutionListResponseDto responseDto = WorkflowExecutionListResponseDto
        .builder()
        .workflowExecutions(StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList()))
        .build();
    responseDto.setGoodState("Found " + responseDto.getWorkflowExecutions().size() + " Workflow(s) registered.");
    return responseDto;
  }

  @Override
  public WorkflowExecutionResponseDto run(WorkflowExecutionRequestDto requestDto, String token) {
    WorkflowExecutionResponseDto responseDto = WorkflowExecutionResponseDto.builder().build();
    WorkflowDto workflowDto = workflowService.findById(requestDto.getWorkflowId());
    if(workflowDto == null || workflowDto.countSteps() == 0){
      responseDto.setBadState("Workflow not found or has 0 steps!");
      return responseDto;
    }

    AlgorithmDto algorithmDto = workflowDto.getAlgorithms().iterator().next();

    DomainModelDto domainModelDto = domainModelService.findById(requestDto.getDomainModelId(), token);
    if(domainModelDto == null){
      responseDto.setBadState("An error happened while trying to get the domain model with id " + requestDto.getDomainModelId() +
          ". Does it exit?");
      return responseDto;
    }
    WorkflowExecution workflowExecution = saveWorkflowExecution(requestDto, workflowDto);
    boolean startedExecutionSuccessfully = artificialSystemService.execute(ArtificialSystemRequestDto.builder()
            .workflowExecutionId(workflowExecution.getId())
            .algorithm(algorithmDto)
            .domainModel(domainModelDto)
        .build());
    if(!startedExecutionSuccessfully){
      responseDto.setBadState("An error happened while trying send the first request to the Artificial System");
      return responseDto;
    }

    LogDto log = registerExecutionLog(workflowExecution, algorithmDto.getName(), domainModelDto.getTopology());

    responseDto = WorkflowExecutionResponseDto.builder()
        .workflowExecution(workflowExecution)
        .logs(Stream.of(log).collect(Collectors.toSet()))
        .build();
    responseDto.setGoodState(
        "Workflow execution started successfully. Check its status with the method findById passing: "
            + workflowExecution.getId());
    return responseDto;
  }



  private WorkflowExecution saveWorkflowExecution(WorkflowExecutionRequestDto requestDto, WorkflowDto workflowDto) {
    WorkflowExecution workflowExecution = WorkflowExecution.builder()
        .workflowId(requestDto.getWorkflowId())
        .domainModelId(requestDto.getDomainModelId())
        .userId(requestDto.getUserId())
        .description(requestDto.getDescription())
        .steps(workflowDto.countSteps())
        .currentStep(1)
        .build();
    return workflowExecutionRepository.save(workflowExecution);
  }

  public LogDto registerExecutionLog(WorkflowExecution workflowExecution, String algorithmName, String data) {
    String message = "Workflow execution id: " + workflowExecution.getId() + ". Step " + workflowExecution.getCurrentStep() + " of " + workflowExecution.getSteps()
        + ". Algorithm being processed: " + algorithmName + ". Domain topology/file: " + data + ".";
    Log log = Log.builder()
        .action(workflowExecution.getDescription())
        .message(message)
        .workflowExecution(workflowExecution)
        .build();
    logRepository.save(log);

    monitoringService.registerEvent(MonitoringLogMessageDto
        .builder()
            .serviceName("MAS Controller")
            .messageType("INFORMATION")
            .operationType("UPDATE")
            .message(message)
        .build());

    return LogDto.builder()
        .action(log.getAction())
        .message(log.getMessage())
        .build();
  }

  private Set<LogDto> getLogs(WorkflowExecution workflowExecution) {
    Set<LogDto> logDtos = new HashSet<>();
    Set<Log> logs = logRepository.findByWorkflowExecution(workflowExecution);
    logs.forEach(log -> logDtos.add(LogDto.builder()
        .action(log.getAction())
        .message(log.getMessage())
        .build()));
    return logDtos;
  }
}