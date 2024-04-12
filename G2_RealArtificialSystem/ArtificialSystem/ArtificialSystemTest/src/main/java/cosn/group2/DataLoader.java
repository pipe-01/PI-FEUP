package cosn.group2;

import cosn.group2.Model.Experience;
import cosn.group2.Model.Task;
import cosn.group2.Repository.ExperienceRepository;
import cosn.group2.Repository.TaskRepository;
import cosn.group2.Service.TaskService;
import cosn.group2.dto.Algorithm;
import cosn.group2.dto.DomainModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataLoader implements ApplicationRunner {

    private TaskRepository taskRepository;
    private ExperienceRepository experienceRepository;
    private TaskService taskService;

    @Autowired
    public DataLoader(TaskService taskRepository, ExperienceRepository experienceRepository) {
        this.taskService = taskRepository;
        this.experienceRepository = experienceRepository;
    }

    public void run(ApplicationArguments args) {
        //removeData();

        Set<Experience> experience = experience();
        Set<Task> tasks = task();
    }

    public void removeData(){
        taskRepository.deleteAll();
        experienceRepository.deleteAll();
    }

    private Set<Task> task() {
        Set<Task> task = new LinkedHashSet<>();
        taskService.executeTask(1,
                new Algorithm("a25943c4-3f22-4715-82e7-581e1591481c", "Algoritmo1",
                        "Descrição do algoritmo1", "Comando - algoritmo1"),
                new DomainModel(1,"a", 1),
                null);

        taskService.executeTask(1,
                new Algorithm("a25943c4-3f22-4715-82e7-581e1591481c", "Algoritmo1",
                        "Descrição do algoritmo1", "Comando - algoritmo1"),
                null, "1- step1.txt");
        /*task.add(taskRepository.save(new Random().nextLong(), 1,
                    String.valueOf(System.currentTimeMillis()),String.valueOf(Math.random()),
                    "G-Unit", filename);*/

        return task;
    }

    private Set<Experience> experience() {
        Set<Experience> experiences = new LinkedHashSet<>();
            //contentManagerRepository.deleteAll();
        experiences.add(new Experience(1));
        return experiences;
    }
}