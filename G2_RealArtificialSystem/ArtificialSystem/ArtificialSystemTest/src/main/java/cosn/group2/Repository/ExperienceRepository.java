package cosn.group2.Repository;

import cosn.group2.Model.Experience;
import cosn.group2.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {

}