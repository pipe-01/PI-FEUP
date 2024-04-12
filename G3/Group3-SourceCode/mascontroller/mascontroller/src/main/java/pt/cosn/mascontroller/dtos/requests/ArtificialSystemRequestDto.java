package pt.cosn.mascontroller.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.cosn.mascontroller.dtos.AlgorithmDto;
import pt.cosn.mascontroller.dtos.DomainModelDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArtificialSystemRequestDto {

  private Long workflowExecutionId;

  private AlgorithmDto algorithm;

  private DomainModelDto domainModel;

  private String path;

}
