package pt.up.fe.cosn.gateway.advices.responses.algorithmsResponses;

import lombok.Getter;
import lombok.Setter;

public class AlgorithmResponse {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private String command;
    @Getter
    @Setter
    private String createdAt;
    @Getter
    @Setter
    private String updatedAt;
}
