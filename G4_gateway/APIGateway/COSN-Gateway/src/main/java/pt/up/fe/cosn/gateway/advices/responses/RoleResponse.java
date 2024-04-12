package pt.up.fe.cosn.gateway.advices.responses;

import lombok.Getter;
import lombok.Setter;
import pt.up.fe.cosn.gateway.entities.Role;

public class RoleResponse extends SimpleResponse{
    @Getter
    @Setter
    private Role role;

    public RoleResponse(Boolean success, String message, Role role){
        super(success, message);
        this.role = role;
    }
}
