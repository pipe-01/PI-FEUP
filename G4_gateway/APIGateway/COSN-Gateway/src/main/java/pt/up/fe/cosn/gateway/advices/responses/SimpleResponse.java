package pt.up.fe.cosn.gateway.advices.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
public class SimpleResponse {
    @Getter
    @Setter
    private Boolean success;
    @Getter
    @Setter
    private String message;

    @Override
    public String toString() {
        return "SimpleResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
