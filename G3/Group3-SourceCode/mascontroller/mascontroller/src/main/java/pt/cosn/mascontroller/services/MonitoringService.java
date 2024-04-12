package pt.cosn.mascontroller.services;

import pt.cosn.mascontroller.dtos.requests.MonitoringLogMessageDto;

public interface MonitoringService {

  void registerEvent(MonitoringLogMessageDto monitoringLogMessageDto);

}
