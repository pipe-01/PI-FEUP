package pt.feup.cosn.monitoring.dto;

import pt.feup.cosn.monitoring.model.serviceLog;

import java.util.List;

public class LogsResponse extends SimpleResponse{
    List<serviceLog> serviceLogList;

    public List<serviceLog> getServiceLogList() {
        return serviceLogList;
    }

    public void setServiceLogList(List<serviceLog> serviceLogList) {
        this.serviceLogList = serviceLogList;
    }
}
