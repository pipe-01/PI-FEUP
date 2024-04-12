package pt.cosn.mascontroller.dtos;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkflowDto {

  private String id;

  private List<AlgorithmDto> algorithms;

  public int countSteps(){
    return CollectionUtils.isEmpty(algorithms) ? 0: algorithms.size();
  }
}
