package cosn.group2.Repository;

import cosn.group2.Model.Experience;
import cosn.group2.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findByExperienceID(Experience id);
}