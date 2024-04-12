package pt.up.fe.cosn.gateway.advices.responses;

import lombok.Getter;
import lombok.Setter;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;

public class LoginResponse extends SimpleResponse {
    @Getter
    @Setter
    private String token;

    public LoginResponse(Boolean success, String message, String token){
        super(success, message);
        this.token = token;
    }
}
