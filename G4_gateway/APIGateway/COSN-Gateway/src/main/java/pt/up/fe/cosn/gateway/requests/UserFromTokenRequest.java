package pt.up.fe.cosn.gateway.requests;

import lombok.Getter;
import lombok.Setter;

public class UserFromTokenRequest {
    @Getter
    @Setter
    private String token;
}
