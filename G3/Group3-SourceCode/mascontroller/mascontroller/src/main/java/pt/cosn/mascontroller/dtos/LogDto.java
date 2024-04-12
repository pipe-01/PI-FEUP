package pt.cosn.mascontroller.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogDto {

  private String action;

  private String message;

}
