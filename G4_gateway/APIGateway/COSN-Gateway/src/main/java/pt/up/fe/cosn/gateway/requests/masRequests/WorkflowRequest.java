package pt.up.fe.cosn.gateway.requests.masRequests;

import lombok.Getter;
import lombok.Setter;

public class WorkflowRequest {
    @Getter
    @Setter
    private String workflowId;
    @Getter
    @Setter
    private String domainModelId;
    @Getter
    @Setter
    private String description;
}
