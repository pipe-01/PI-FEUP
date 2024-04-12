package pt.feup.cosn.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.feup.cosn.monitoring.Utils.SimpleWrapper;
import pt.feup.cosn.monitoring.dto.LogsResponse;
import pt.feup.cosn.monitoring.model.service;
import pt.feup.cosn.monitoring.model.serviceLog;
import pt.feup.cosn.monitoring.dto.SimpleResponse;
import pt.feup.cosn.monitoring.services.serviceLogsServices;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class serviceLogController {
    private final serviceLogsServices serLogSer;

    @Autowired
    public serviceLogController(serviceLogsServices serLogSer) {
        this.serLogSer = serLogSer;
    }

    @CrossOrigin
    @PostMapping("/logs")
    public SimpleResponse addLogs(@RequestBody SimpleWrapper wrapper){
        SimpleResponse sr = SimpleResponse.createNew();

        service ser = wrapper.getSer();
        serviceLog serLog = wrapper.getSerLog();

        if (serLogSer.addLog(ser,serLog)){
            sr.setAsSuccess("Log added successfully");
        }

        return sr;
    }

    @CrossOrigin
    @DeleteMapping("/logs")
    public SimpleResponse deleteLogs(@RequestBody serviceLog serLog){
        SimpleResponse sr = SimpleResponse.createNew();

        if (serLogSer.deleteLog(serLog)){
            sr.setAsSuccess("Log deleted successfully");
        }

        return sr;
    }

    @CrossOrigin
    @GetMapping("/logs")
    public List<serviceLog> getLogs(){
        return serLogSer.getLogs();
    }

    @CrossOrigin
    @PostMapping("/logsByService")
    public SimpleResponse getLogsByServiceID(@RequestBody SimpleWrapper simpleWrapper){
        LogsResponse sr = new LogsResponse();

        service ser = simpleWrapper.getSer();

        if(ser.getId()==null){
            sr.setAsError("Service does not exist");
            return sr;
        }

        List<serviceLog> ServiceLogs = serLogSer.getLogsByService(ser);

        sr.setAsSuccess();
        sr.setServiceLogList(ServiceLogs);

        return sr;
    }

    @CrossOrigin
    @PostMapping("/logsByServiceTimestamp")
    public SimpleResponse getLogsByServiceTimestamp(@RequestBody SimpleWrapper simpleWrapper){
        LogsResponse sr = new LogsResponse();

        Timestamp init = simpleWrapper.getInitialTimestamp();
        Timestamp end = simpleWrapper.getEndTimestamp();
        service ser = simpleWrapper.getSer();

        if(init==null || end==null || init.after(end)){
            sr.setAsError("Invalid timestamp");
            return sr;
        }

        if(ser==null || ser.getId()==null){
            sr.setAsError("Invalid service");
            return sr;
        }

        List<serviceLog> ServiceLogs = serLogSer.getLogsByServiceAndTimestamp(init, end, ser);

        sr.setAsSuccess();
        sr.setServiceLogList(ServiceLogs);

        return sr;
    }

}
