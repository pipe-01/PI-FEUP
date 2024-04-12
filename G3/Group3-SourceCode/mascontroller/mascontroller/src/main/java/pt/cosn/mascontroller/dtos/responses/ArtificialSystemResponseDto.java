package pt.cosn.mascontroller.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArtificialSystemResponseDto {

  private Long workflowExecutionId;

  private String path;

}
