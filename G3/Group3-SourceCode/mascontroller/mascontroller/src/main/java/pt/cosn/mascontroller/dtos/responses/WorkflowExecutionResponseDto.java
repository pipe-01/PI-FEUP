package pt.cosn.mascontroller.dtos.responses;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.cosn.mascontroller.dtos.LogDto;
import pt.cosn.mascontroller.models.WorkflowExecution;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkflowExecutionResponseDto extends GenericResponseDto {

  private WorkflowExecution workflowExecution;

  private Set<LogDto> logs;

}
