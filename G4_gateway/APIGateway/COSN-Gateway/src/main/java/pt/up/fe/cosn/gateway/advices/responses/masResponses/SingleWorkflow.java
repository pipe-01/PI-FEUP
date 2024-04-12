package pt.up.fe.cosn.gateway.advices.responses.masResponses;

import lombok.Getter;
import lombok.Setter;

public class SingleWorkflow {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String workflowId;
    @Getter
    @Setter
    private String domainModelId;
    @Getter
    @Setter
    private String userId;
    @Getter
    @Setter
    private String description;
}
