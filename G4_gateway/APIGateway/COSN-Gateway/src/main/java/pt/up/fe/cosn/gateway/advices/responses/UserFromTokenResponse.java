package pt.up.fe.cosn.gateway.advices.responses;

import lombok.Getter;
import lombok.Setter;
import pt.up.fe.cosn.gateway.entities.User;

public class UserFromTokenResponse extends SimpleResponse{
    @Getter
    @Setter
    private User user;

    public UserFromTokenResponse(Boolean success, String message, User user){
        super(success, message);
        this.user = user;
    }
}
