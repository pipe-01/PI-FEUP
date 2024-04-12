package pt.feup.cosn.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.feup.cosn.monitoring.dto.ServiceResponse;
import pt.feup.cosn.monitoring.model.service;
import pt.feup.cosn.monitoring.dto.SimpleResponse;
import pt.feup.cosn.monitoring.services.UtilsServices;
import pt.feup.cosn.monitoring.services.serviceServices;


@RestController
public class serviceController {
    private final serviceServices serServices;
    private final UtilsServices utilsServices;

    @Autowired
    public serviceController(serviceServices serServices, UtilsServices utilsServices){
        this.serServices = serServices;
        this.utilsServices = utilsServices;
    }

    @CrossOrigin
    @PostMapping("/registerService")
    public SimpleResponse register(@RequestBody service ser){
        SimpleResponse sr = SimpleResponse.createNew();

        if (serServices.registerService(ser)){
            sr.setAsSuccess("Service registered successfully");
        }

        return sr;
    }

    @CrossOrigin
    @GetMapping("/services")
    public SimpleResponse getService(){
        ServiceResponse sr = new ServiceResponse();
        sr.setAsSuccess();
        sr.setServices(serServices.getAllService());
        return sr;
    }

    @CrossOrigin
    @PutMapping("/services")
    public SimpleResponse updateService(@RequestBody service ser){
        SimpleResponse sr = SimpleResponse.createNew();

        if (serServices.updateService(ser)){
            sr.setAsSuccess("Service updated successfully");
        }

        return sr;
    }

    @CrossOrigin
    @DeleteMapping("/services")
    public SimpleResponse deleteService(@RequestBody service ser){
        SimpleResponse sr = SimpleResponse.createNew();

        if (utilsServices.deleteService(ser)){
            sr.setAsSuccess("Service deleted successfully");
        }

        return sr;
    }

}
