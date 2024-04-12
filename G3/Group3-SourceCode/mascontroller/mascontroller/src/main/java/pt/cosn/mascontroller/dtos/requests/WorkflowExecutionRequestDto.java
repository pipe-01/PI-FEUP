package pt.cosn.mascontroller.dtos.requests;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class WorkflowExecutionRequestDto {

  @NotEmpty(message = "Workflow Id name cannot be empty")
  private String workflowId;

  @NotEmpty(message = "Domain Model Id name cannot be empty")
  private String domainModelId;

  @NotEmpty(message = "User id cannot be empty")
  private String userId;

  private String description;

}
