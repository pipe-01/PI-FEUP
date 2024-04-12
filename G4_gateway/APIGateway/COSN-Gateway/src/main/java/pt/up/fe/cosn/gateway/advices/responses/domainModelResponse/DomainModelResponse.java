package pt.up.fe.cosn.gateway.advices.responses.domainModelResponse;

import lombok.Getter;
import lombok.Setter;

public class DomainModelResponse {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private String modelType;
}
