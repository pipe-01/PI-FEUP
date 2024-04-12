package pt.up.fe.cosn.gateway.advices.responses.masResponses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MyWorkflowResponse extends WorkflowResponse {
    @Getter
    @Setter
    private List<SingleWorkflow> workflowExecutions;
}
