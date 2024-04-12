package cosn.group2.Service;

import cosn.group2.Model.Experience;
import cosn.group2.Model.Log;
import cosn.group2.Model.Task;
import cosn.group2.Repository.ExperienceRepository;
import cosn.group2.Repository.TaskRepository;
import cosn.group2.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository repository;

    @Autowired
    private KafkaProducer kafkaProducer;


    public Experience newExperience(int experience){

        //Experincia com DomainModel preenchido
        //Task com DomainModel a null
        System.out.println(experience);
        Experience experience1= new Experience(experience);
        repository.save(experience1);

        //Topic Monitoring System
        Log log = Log.instance(Log.LogType.INFORMATION,
                Log.OperationType.CREATE, "Created new Workflow");
        kafkaProducer.log(log);

        return experience1;
    }

    public Optional<Experience> findByID(int id){
        return repository.findById(id);
    }

}
