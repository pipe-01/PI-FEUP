package pt.cosn.mascontroller.services;

import pt.cosn.mascontroller.dtos.WorkflowDto;

public interface WorkflowService {

  WorkflowDto findById(String id);

}
