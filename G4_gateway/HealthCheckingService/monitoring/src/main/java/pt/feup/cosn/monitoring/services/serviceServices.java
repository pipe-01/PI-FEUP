package pt.feup.cosn.monitoring.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.feup.cosn.monitoring.Utils.LoadFromFile;
import pt.feup.cosn.monitoring.model.service;
import pt.feup.cosn.monitoring.model.serviceLog;
import pt.feup.cosn.monitoring.repository.serviceRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class serviceServices {
    private final serviceRepository serRepo;

    @Autowired
    public serviceServices (serviceRepository serRepo){
        this.serRepo = serRepo;
        LoadFromFile();

    }

    private void LoadFromFile() {
        List<JSONObject> listOfServices = LoadFromFile.getInstance().convertServiceFromFile();

        try {
            for (JSONObject jsonObject: listOfServices) {
                service ser = new service(jsonObject.getString("name"),jsonObject.getString("ip"), jsonObject.getString("port"));

                serRepo.save(ser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean registerService(service ser){
        serRepo.save(ser);
        return true;
    }

    public List<service> getAllService(){
        List<service> servicos = serRepo.findAll();
        return servicos ;
    }

    public boolean updateService(service ser){
        if (ser.getId() != null){
            Optional<service> serviceOptional = serRepo.findById(ser.getId());
            if(serviceOptional.isPresent()){
                service savedService = serviceOptional.get();
                savedService.clone(ser);
                serRepo.save(savedService);
                return true;
            }
        }
        return false;
    }

}
