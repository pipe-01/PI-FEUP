package pt.cosn.mascontroller.dtos.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkflowExecutionListRequestDto {

  @NotEmpty(message = "User id cannot be empty")
  private String userId;

}
