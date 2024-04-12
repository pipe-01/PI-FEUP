package pt.up.fe.cosn.gateway.requests;

import lombok.Getter;
import lombok.Setter;

public class UserRoleChangeRequest {
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private Long roleId;
}
