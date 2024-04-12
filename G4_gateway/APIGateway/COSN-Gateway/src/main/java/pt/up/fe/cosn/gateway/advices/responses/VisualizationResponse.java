package pt.up.fe.cosn.gateway.advices.responses;

import lombok.Getter;
import lombok.Setter;

public class VisualizationResponse {
    @Getter
    @Setter
    private String response;

    public VisualizationResponse(String response){
        this.response = response;
    }
}
