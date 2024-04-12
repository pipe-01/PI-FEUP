package pt.up.fe.cosn.gateway.requests;

import lombok.Getter;
import lombok.Setter;

public class UserRegisterRequest {
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private Long roleId;
}
