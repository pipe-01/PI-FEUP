package pt.up.fe.cosn.gateway.advices.responses.masResponses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RunWorkflowResponse extends WorkflowResponse{
    @Getter
    @Setter
    private SingleWorkflow workflowExecution;
    @Getter
    @Setter
    private List<MASLog> logs;
}
