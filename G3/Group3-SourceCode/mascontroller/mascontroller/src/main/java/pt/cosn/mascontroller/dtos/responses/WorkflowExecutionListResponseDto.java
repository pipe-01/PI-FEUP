package pt.cosn.mascontroller.dtos.responses;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import pt.cosn.mascontroller.models.WorkflowExecution;

@Builder
@Data
public class WorkflowExecutionListResponseDto extends GenericResponseDto {

  private List<WorkflowExecution> workflowExecutions;

}
