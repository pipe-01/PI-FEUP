package pt.up.fe.cosn.gateway.advices.responses.masResponses;

import lombok.Getter;
import lombok.Setter;

public class MASLog {
    @Getter
    @Setter
    private String action;
    @Getter
    @Setter
    private String message;
}
