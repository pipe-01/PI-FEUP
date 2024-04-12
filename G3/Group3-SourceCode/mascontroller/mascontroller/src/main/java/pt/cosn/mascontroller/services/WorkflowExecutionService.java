package pt.cosn.mascontroller.services;

import java.util.Optional;
import pt.cosn.mascontroller.dtos.requests.WorkflowExecutionRequestDto;
import pt.cosn.mascontroller.dtos.responses.WorkflowExecutionListResponseDto;
import pt.cosn.mascontroller.dtos.responses.WorkflowExecutionResponseDto;
import pt.cosn.mascontroller.models.WorkflowExecution;

public interface WorkflowExecutionService {

  WorkflowExecutionResponseDto findById(Long id);

  Optional<WorkflowExecution> findWorkflowExecutionById(Long id);

  WorkflowExecutionListResponseDto findByUserId(String userId);

  WorkflowExecutionResponseDto run(WorkflowExecutionRequestDto executionRequestDto, String token);

}
