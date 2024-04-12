package pt.feup.cosn.monitoring.services;

import org.springframework.stereotype.Service;
import pt.feup.cosn.monitoring.model.service;
import pt.feup.cosn.monitoring.model.serviceLog;
import pt.feup.cosn.monitoring.repository.serviceLogsRepository;
import pt.feup.cosn.monitoring.repository.serviceRepository;

import java.util.Optional;

@Service
public class UtilsServices {

    private final serviceRepository serRepo;
    private final serviceLogsRepository serLogRepo;

    public UtilsServices(serviceRepository serRepo, serviceLogsRepository serLogRepo) {
        this.serRepo = serRepo;
        this.serLogRepo = serLogRepo;
    }

    public boolean deleteService(service ser) {
        if (ser.getId() != null){
            Optional<service> serviceOptional = serRepo.findById(ser.getId());
            if(serviceOptional.isPresent()){

                for ( serviceLog serLog: serviceOptional.get().getLogs()) {
                    serLogRepo.delete(serLog);
                }
                serRepo.delete(serviceOptional.get());
                return true;
            }
        }
        return false;
    }
}
