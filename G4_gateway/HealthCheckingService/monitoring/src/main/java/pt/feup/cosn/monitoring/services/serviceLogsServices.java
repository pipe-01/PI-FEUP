package pt.feup.cosn.monitoring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.feup.cosn.monitoring.model.service;
import pt.feup.cosn.monitoring.model.serviceLog;
import pt.feup.cosn.monitoring.repository.serviceLogsRepository;
import pt.feup.cosn.monitoring.repository.serviceRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class serviceLogsServices {

    private final serviceRepository serRepo;
    private final serviceLogsRepository serLogRepo;

    @Autowired
    public serviceLogsServices(serviceRepository serRepo, serviceLogsRepository serLogRepo){
        this.serRepo = serRepo;
        this.serLogRepo = serLogRepo;
    }

    public boolean addLog(service ser, serviceLog serLog){
        Optional<service> optionalService = serRepo.findById(ser.getId());

        if(optionalService.isEmpty()){
            return false;
        }

        serLog.setSer(optionalService.get());
        serviceLog saveServiceLog = serLogRepo.save(serLog);

        optionalService.get().addSerLog(saveServiceLog);

        //Verificar se é preciso
        serRepo.save(optionalService.get());

        return true;
    }


    public boolean deleteLog(serviceLog serLog) {
        if (serLog.getId() == null){
            return false;
        }

        Optional<serviceLog> optionalServiceLog = serLogRepo.findById(serLog.getId());

        if (optionalServiceLog.isEmpty()){
            return false;
        }

        serLogRepo.delete(optionalServiceLog.get());

        return true;
    }

    public List<serviceLog> getLogs() {
        return serLogRepo.findAll();
    }

    public List<serviceLog> getLogsByService(service ser) {
        if (ser.getId() == null || serRepo.findById(ser.getId()).isEmpty()){
            return Collections.EMPTY_LIST;
        }

        return serRepo.findById(ser.getId()).get().getLogs();
    }


    public List<serviceLog> getLogsByTimestamp(Timestamp init, Timestamp end) {
        List<serviceLog> listServiceLog = serLogRepo.findAll();

        List<serviceLog> resultList = new ArrayList<>();

        for (serviceLog serLog: listServiceLog ) {
            if (serLog.getTimestamp().after(init) && serLog.getTimestamp().before(end)){
                resultList.add(serLog);
            }
        }
        return resultList;
    }

    public List<serviceLog> getLogsByServiceAndTimestamp(Timestamp init, Timestamp end, service ser) {
        List<serviceLog> resultList = new ArrayList<>();

        Optional<service> optionalService = serRepo.findById(ser.getId());

        if (optionalService.isEmpty()){
            return Collections.EMPTY_LIST;
        }

        List<serviceLog> serviceLogList = optionalService.get().getLogs();

        for (serviceLog serLog: serviceLogList ) {
            if (serLog.getTimestamp().after(init) && serLog.getTimestamp().before(end)){
                resultList.add(serLog);
            }
        }
        return resultList;
    }
}
