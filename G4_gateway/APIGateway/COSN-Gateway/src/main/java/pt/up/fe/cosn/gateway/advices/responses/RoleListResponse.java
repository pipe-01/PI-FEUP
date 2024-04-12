package pt.up.fe.cosn.gateway.advices.responses;

import lombok.Getter;
import lombok.Setter;
import pt.up.fe.cosn.gateway.entities.Role;

import java.util.List;

public class RoleListResponse extends SimpleResponse{
    @Getter
    @Setter
    private List<Role> roleList;

    public RoleListResponse(Boolean success, String message, List<Role> roleList){
        super(success, message);
        this.roleList = roleList;
    }
}
