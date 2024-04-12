package pt.up.fe.cosn.gateway.advices.responses.masResponses;

import lombok.Getter;
import lombok.Setter;

public class WorkflowResponse {
    @Getter
    @Setter
    private String error;
    @Getter
    @Setter
    private String message;
}
