package pt.cosn.mascontroller.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringLogMessageDto {

  private String serviceName;

  private String operationType;

  private String messageType;

  private String message;

}
