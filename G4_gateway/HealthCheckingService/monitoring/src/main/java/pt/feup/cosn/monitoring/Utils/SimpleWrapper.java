package pt.feup.cosn.monitoring.Utils;

import pt.feup.cosn.monitoring.model.service;
import pt.feup.cosn.monitoring.model.serviceLog;

import java.sql.Timestamp;

public class SimpleWrapper {
    private service service;
    private serviceLog serviceLog;
    private Timestamp initialTimestamp, endTimestamp;

    public Timestamp getInitialTimestamp() {
        return initialTimestamp;
    }

    public void setInitialTimestamp(Timestamp initialTimestamp) {
        this.initialTimestamp = initialTimestamp;
    }

    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public service getSer() {
        return service;
    }

    public void setSer(service ser) {
        this.service = ser;
    }

    public serviceLog getSerLog() {
        return serviceLog;
    }

    public void setSerLog(serviceLog serLog) {
        this.serviceLog = serLog;
    }
}
