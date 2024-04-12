package pt.cosn.mascontroller.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlgorithmDto {

  private String id;

  private String name;

  private String description;

  private String command;

  private int order;
}
