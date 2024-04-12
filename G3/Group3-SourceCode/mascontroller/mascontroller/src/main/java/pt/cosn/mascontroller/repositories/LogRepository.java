package pt.cosn.mascontroller.repositories;

import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import pt.cosn.mascontroller.models.WorkflowExecution;
import pt.cosn.mascontroller.models.Log;

public interface LogRepository extends CrudRepository<Log, Long> {

  Set<Log> findByWorkflowExecution(WorkflowExecution workflowExecution);
}
