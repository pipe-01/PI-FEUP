package pt.cosn.mascontroller.dtos.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowExecutionStatusRequestDto {

  @NotNull(message = "workflowExecutionId id cannot be null")
  private Long workflowExecutionId;

  @NotEmpty(message = "User Id name cannot be empty")
  private String userId;

}
