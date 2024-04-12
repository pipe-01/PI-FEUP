package pt.cosn.mascontroller.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import pt.cosn.mascontroller.models.WorkflowExecution;

public interface WorkflowExecutionRepository extends CrudRepository<WorkflowExecution, Long> {
  List<WorkflowExecution> findByUserId(String userId);


}
