package cosn.group2.Service;

import cosn.group2.Model.Experience;
import cosn.group2.Model.Log;
import cosn.group2.Model.Task;
import cosn.group2.Repository.ExperienceRepository;
import cosn.group2.Repository.TaskRepository;
import cosn.group2.dto.Algorithm;
import cosn.group2.dto.DomainModel;
import cosn.group2.dto.MASResponse;
import cosn.group2.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository repository;

    /*@Autowired
    private ExperienceService experienceService;*/

    @Autowired
    private KafkaProducer kafkaProducer;

    private final String FILE_PATH = "Files/";

    @Autowired
    private ExperienceRepository experienceRepository;

    public void executeTask(int experienceID, Algorithm algorithm, DomainModel domainModel, String filePath){

        String FILE_NAME;
        Task task=new Task();

        //É o 1º Passo (Experiencia) -> Criar ficheiro
        if(filePath==null){
            //Guarda experiencia na BD
            //Experience experience =experienceService.newExperience(experienceID);
            Experience experience = experienceRepository.save(new Experience(experienceID));
            FILE_NAME = experienceID+"- step1.txt";
            task.execute(experience, FILE_NAME);

            //Apaga ficheiro anterior
            createFile(FILE_PATH, FILE_NAME, task.toString() +"\n"+
                    algorithm.toString() + "\n" + domainModel.toString());

            //Topic Monitoring System
            Log log = Log.instance(Log.LogType.INFORMATION,
                    Log.OperationType.CREATE, "Created new Workflow");
            kafkaProducer.log(log);
        }
        else{
            FILE_NAME = experienceID+"- Takes '" + filePath +"' as argument.txt";
            task.execute(new Experience(experienceID), FILE_NAME);

            //Apaga ficheiro anterior
            createFile(FILE_PATH, FILE_NAME, task.toString() +"\n"+
                    algorithm.toString());

        }
        repository.save(task);

        //Topic MAS
        kafkaProducer.sendTask("{\"workflowExecutionId\": "+experienceID+" ,\"path\": \""+ task.getFilename()+"\"}");

        //Topic Monitoring System
        Log log = Log.instance(Log.LogType.INFORMATION,
                Log.OperationType.CREATE, "Created new Task");
        System.out.println(kafkaProducer.log(log));

    }

    public List<String> getAllTasks(int experience){

        List<Task> list=repository.findByExperienceID(new Experience(experience));

        //Topic Monitoring System
        Log log = Log.instance(Log.LogType.INFORMATION,
                Log.OperationType.READ, "Tasks files names returned");
        System.out.println(kafkaProducer.log(log));

        return list.stream().map(Task::getFilename).collect(Collectors.toList());
    }


    public String getExperienceContent(int experience){
        List<String> files=repository.findByExperienceID(new Experience(experience))
                .stream().map(Task::getFilename).collect(Collectors.toList());

        if(files.isEmpty())
            return null;

        try(FileOutputStream experienceFile =
                    new FileOutputStream(FILE_PATH+"Experience " +experience +".txt")){
            for(String str : files) {
                BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH + str));

                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    experienceFile.write((currentLine+System.lineSeparator()).getBytes());
                }

                reader.close();
            }

            //Topic Monitoring System
            Log log = Log.instance(Log.LogType.INFORMATION,
                    Log.OperationType.READ, "Workflow content returned");
            kafkaProducer.log(log);

            //return experienceFile;
            return FILE_PATH+"Experience " +experience +".txt";

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void createFile(String FILE_PATH, String FILE_NAME, String fileContent){
        //Apaga ficheiro anterior
        try(FileOutputStream fileOutputStream =
                    new FileOutputStream(FILE_PATH+FILE_NAME)){
            String str=fileContent;
            byte[] strToBytes = str.getBytes();
            fileOutputStream.write(strToBytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*Path newFilePath = Paths.get(FILE_PATH+FILE_NAME);
            try {
                Files.createFile(newFilePath);
            } catch (FileAlreadyExistsException ex) {
                ex.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }*/
    }




}
