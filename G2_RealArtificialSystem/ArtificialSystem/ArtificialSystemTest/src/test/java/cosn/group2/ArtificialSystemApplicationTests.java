package cosn.group2;

import cosn.group2.Model.Experience;
import cosn.group2.Model.Task;
import cosn.group2.Service.TaskService;
import cosn.group2.dto.Algorithm;
import cosn.group2.dto.DomainModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ArtificialSystemApplicationTests {

	@Autowired
	private TaskService service;

	@Test
	void executeTask_NewExperience() {
		service.executeTask(
				1,
				new Algorithm("a25943c4-3f22-4715-82e7-581e1591481c", "Algoritmo1",
						"Descrição do algoritmo1", "Comando - algoritmo1"),
				new DomainModel(1,"a", 1),
				null);
	}

	@Test
	void executeTask_NewTask() {
		service.executeTask(1,
				new Algorithm("a25943c4-3f22-4715-82e7-581e1591481c", "Algoritmo1",
						"Descrição do algoritmo1", "Comando - algoritmo1"),
				null, "1- step1.txt");

	}

	@Test
	void getAllTasks() {
		List<String> fileNames=service.getAllTasks(1);
		System.out.println(fileNames);
	}

	@Test
	void getExperienceContent() {
		service.getExperienceContent(1);
		//System.out.println(fileNames);
	}

}
