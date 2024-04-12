package pt.feup.cosn.monitoring.dto;

import pt.feup.cosn.monitoring.model.service;

import java.util.List;

public class ServiceResponse extends SimpleResponse{
    List<service> services;

    public List<service> getServices() {
        return services;
    }

    public void setServices(List<service> services) {
        this.services = services;
    }
}
